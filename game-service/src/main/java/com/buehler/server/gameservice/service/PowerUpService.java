package com.buehler.server.gameservice.service;

import com.buehler.server.gameservice.entities.PowerUp;
import com.buehler.server.gameservice.entities.PowerUpType;
import com.buehler.server.gameservice.entities.StickFigure;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PowerUpService {

    private final Map<Long, Map<Long, PowerUp>> purchasedPowerUps = new HashMap<>();

    // Allow users to purchase a power-up
    public boolean purchasePowerUp(Long gameId, Long fighterId, PowerUpType powerUpType) {
        PowerUp powerUp = createPowerUp(powerUpType);

        // If the user has enough credits or payment is handled successfully
        //TODO: implmenet a payment service as a separate microservice
        if (true) { // Placeholder for actual payment logic

            // Store the purchased power-up for the specific game and fighter
            purchasedPowerUps
                    .computeIfAbsent(gameId, _ -> new HashMap<>())
                    .put(fighterId, powerUp);
            return true;
        }
        return false;
    }

    public void applyPurchasedPowerUp(Long gameId, StickFigure fighter) {
        PowerUp powerUp = purchasedPowerUps
                .getOrDefault(gameId, new HashMap<>())
                .get(fighter.getId());

        if (powerUp != null && powerUp.isActive()) {
            // Apply the power-up to the fighter
            switch (powerUp.getType()) {
                case BOOST_STRENGTH:
                    fighter.setStrength(fighter.getStrength() + 10);
                    break;
                case INCREASE_DEFENSE:
                    fighter.setDefense(fighter.getDefense() + 10);
                    break;
                case SPEED_BOOST:
                    fighter.setSpeed(fighter.getSpeed() + 5);
                    break;
                case INVINCIBILITY:
                    fighter.setDefense(Integer.MAX_VALUE);  // Temporary invincibility
                    break;
            }
            fighter.applyPowerUp(powerUp); // Add power-up to the active list
        } else if (powerUp != null && !powerUp.isActive()) {
            fighter.removePowerUp(powerUp);
        }
    }

    private PowerUp createPowerUp(PowerUpType type) {
        int duration = 5;  // Example duration for all power-ups
        int cost;
        switch (type) {
            case BOOST_STRENGTH:
                cost = 100;
                break;
            case INCREASE_DEFENSE:
                cost = 100;
                break;
            case SPEED_BOOST:
                cost = 75;
                break;
            case INVINCIBILITY:
                cost = 150;
                break;
            default:
                throw new IllegalArgumentException("Invalid power-up type");
        }
        return new PowerUp(type, duration, cost);
    }
}
