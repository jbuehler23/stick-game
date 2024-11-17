package com.buehler.server.gameservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PowerUpType type;
    private int durationSeconds;
    private int cost;  // New attribute to represent the cost
    private LocalDateTime appliedAt;      // Duration in seconds

    public PowerUp(PowerUpType type, int duration, int cost) {
        this.type = type;
        this.durationSeconds = duration;
        this.cost = cost;
        this.appliedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return appliedAt.plusSeconds(durationSeconds).isAfter(LocalDateTime.now());
    }
}
