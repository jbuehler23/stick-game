package com.buehler.server.gameservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerUpNotificationMessage {
    private Long gameId;
    private Long userId;
    private String team;         // "Red" or "Blue"
    private String powerUpName;  // e.g., "Double Strength"
    private int cost;  // e.g., 50 for a 50% increase in strength
    private int duration;        // Duration of the effect in seconds

}
