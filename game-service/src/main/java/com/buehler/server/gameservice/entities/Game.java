package com.buehler.server.gameservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // "ONGOING" or "COMPLETED"
    private String winner; // "RED" or "BLUE"

    @OneToOne
    private StickFigure redFigure;

    @OneToOne
    private StickFigure blueFigure;

    // Apply a power-up to the red fighter
    public void applyPowerUpToRed(PowerUp powerUp) {
        redFigure.applyPowerUp(powerUp);
    }
    // Apply a power-up to the blue fighter
    public void applyPowerUpToBlue(PowerUp powerUp) {
        blueFigure.applyPowerUp(powerUp);
    }

}
