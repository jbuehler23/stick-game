package com.buehler.server.gameservice.service;

import com.buehler.server.gameservice.entities.*;
import com.buehler.server.gameservice.model.GameStatusMessage;
import com.buehler.server.gameservice.model.PowerUpNotificationMessage;
import com.buehler.server.gameservice.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GameService {
    private final GameRepository gameRepository;

    private final RestTemplate restTemplate;

    private final PowerUpRepository powerUpRepository;

    private final PowerUpService powerUpService;

    private static final int FIGHT_DURATION_MS = 60000; // 1 minute in milliseconds
    private static final int STATUS_EFFECT_INTERVAL_MS = 5000; // Apply a status effect every 5 seconds


    @Value("${websocket.service.url}")
    private String websocketServiceUrl;
    private final Timer statusEffectTimer = new Timer();

    public GameService(GameRepository gameRepository, UserServiceClient userServiceClient, RestTemplate restTemplate, PowerUpRepository powerUpRepository, PowerUpService powerUpService) {
        this.gameRepository = gameRepository;
        this.restTemplate = restTemplate;
        this.powerUpRepository = powerUpRepository;
        this.powerUpService = powerUpService;
    }

    public Game createGame(Long redUserId, Long blueUserId) {
        Game game = new Game();
        StickFigure redFigure = createStickFigure("RED", redUserId);
        StickFigure blueFigure = createStickFigure("BLUE", blueUserId);

        game.setRedFigure(redFigure);
        game.setBlueFigure(blueFigure);
        game.setStatus("ONGOING");

        return gameRepository.save(game);
    }

    private StickFigure createStickFigure(String color, Long id) {
        StickFigure stickFigure = new StickFigure();
        stickFigure.setColor(color);
        stickFigure.assignRandomWeapon();
        stickFigure.setId(id);
        return stickFigure;
    }


    private void notifyGameOutcome(Game game) {
        GameStatusMessage gameStatus = new GameStatusMessage();
        gameStatus.setGameId(game.getId());
        gameStatus.setStatus("COMPLETED");
        gameStatus.setWinner(game.getWinner());
        gameRepository.save(game);

        restTemplate.postForLocation(websocketServiceUrl + "/api/notifications/game/outcome", gameStatus);
    }

    /**
     * Applies a power-up to a fighter in an ongoing game.
     */
    public String applyPowerUp(Long gameId, Long userId, Long powerUpId, String team) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        PowerUp powerUp = powerUpRepository.findById(powerUpId)
                .orElseThrow(() -> new IllegalArgumentException("Power-up not found"));

        if (!"ONGOING".equals(game.getStatus())) {
            return "Power-ups can only be applied in ongoing games.";
        }

        // Apply power-up effect to the specified team
        if ("RED".equalsIgnoreCase(team)) {
            game.applyPowerUpToRed(powerUp);
        } else if ("BLUE".equalsIgnoreCase(team)) {
            game.applyPowerUpToBlue(powerUp);
        } else {
            return "Invalid team specified.";
        }

        gameRepository.save(game);

        // Notify WebSocket Service of the power-up application
        notifyPowerUpApplied(gameId, userId, team, powerUp);

        return "Power-up applied successfully!";
    }

    private void notifyPowerUpApplied(Long gameId, Long userId, String team, PowerUp powerUp) {
        PowerUpNotificationMessage message = new PowerUpNotificationMessage(
                gameId,
                userId,
                team,
                powerUp.getType().name(),
                powerUp.getCost(),
                powerUp.getDurationSeconds()
        );
        restTemplate.postForLocation(websocketServiceUrl + "/api/notifications/power-up-applied", message);
    }

    public void startFight(Game game) {
        game.getRedFigure().assignRandomWeapon();
        game.getBlueFigure().assignRandomWeapon();

        Timer fightTimer = new Timer();

        fightTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Apply purchased power-ups before determining the winner
                powerUpService.applyPurchasedPowerUp(game.getId(), game.getRedFigure());
                powerUpService.applyPurchasedPowerUp(game.getId(), game.getBlueFigure());

                determineWinner(game);
                fightTimer.cancel();
            }
        }, FIGHT_DURATION_MS);

        // Apply random status effects throughout the fight
        statusEffectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                applyRandomStatusEffect(game, "RED");
                applyRandomStatusEffect(game, "BLUE");
            }
        }, 0, STATUS_EFFECT_INTERVAL_MS);
    }

    private void determineWinner(Game game) {
        StickFigure redFigure = game.getRedFigure();
        StickFigure blueFigure = game.getBlueFigure();

        int redTotal = redFigure.getStrength() + redFigure.getDefense() + redFigure.getSpeed();
        int blueTotal = blueFigure.getStrength() + blueFigure.getDefense() + blueFigure.getSpeed();

        if (redTotal > blueTotal) {
            game.setWinner("Red");
        } else if (blueTotal > redTotal) {
            game.setWinner("Blue");
        } else {
            game.setWinner("Draw");
        }

        // Notify users about the outcome
        notifyGameOutcome(game);
    }

    private void applyRandomStatusEffect(Game game, String team) {
        Random random = new Random();
        StatusEffect effect = StatusEffect.values()[random.nextInt(StatusEffect.values().length)];
        if ("RED".equals(team)) {
            applyEffectToFighter(game.getRedFigure(), effect);
        } else {
            applyEffectToFighter(game.getBlueFigure(), effect);
        }
    }

    private void applyEffectToFighter(StickFigure stickFigure, StatusEffect effect) {
        switch (effect) {
            case STUNNED:
                stickFigure.setSpeed(0); // Reset speed
                break;
            case BERSERK:
                stickFigure.setStrength(stickFigure.getStrength() + 20);
                stickFigure.setDefense(stickFigure.getDefense() - 10);
                break;
            case WEAKENED:
                stickFigure.setStrength(stickFigure.getStrength() - 10);
                break;
            case SHIELDED:
                stickFigure.setDefense(stickFigure.getDefense() + 15);
                break;
        }
    }
}
