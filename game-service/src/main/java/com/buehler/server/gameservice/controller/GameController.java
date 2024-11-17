package com.buehler.server.gameservice.controller;

import com.buehler.server.gameservice.entities.Game;
import com.buehler.server.gameservice.entities.PowerUpType;
import com.buehler.server.gameservice.service.GameService;
import com.buehler.server.gameservice.service.PowerUpService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    private final PowerUpService powerUpService;

    public GameController(GameService gameService, PowerUpService powerUpService) {
        this.gameService = gameService;
        this.powerUpService = powerUpService;
    }

    /**
     * Create a new game between two users with given IDs.
     * @param redUserId ID of the red team user.
     * @param blueUserId ID of the blue team user.
     * @return the created Game object.
     */
    @PostMapping("/start")
    public String startGame(@RequestParam Long redUserId, @RequestParam Long blueUserId) {
        Game game = gameService.createGame(redUserId, blueUserId);
        gameService.startFight(game);
        return "Game started with ID: " + game.getId();
    }


    // Endpoint for purchasing a power-up for a fighter in a game
    @PostMapping("/purchase-power-up/{gameId}/{fighterId}")
    public String purchasePowerUp(@PathVariable Long gameId,
                                  @PathVariable Long fighterId,
                                  @RequestBody PowerUpType powerUpType) {
        boolean success = powerUpService.purchasePowerUp(gameId, fighterId, powerUpType);
        if (success) {
            return "Power-up " + powerUpType + " purchased for " + fighterId + " in game ID " + gameId;
        } else {
            return "Insufficient funds or unable to purchase power-up.";
        }
    }

}
