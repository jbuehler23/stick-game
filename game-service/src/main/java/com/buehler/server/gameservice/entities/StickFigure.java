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
public class StickFigure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color; // "RED" or "BLUE"

    @OneToOne
    private Weapon weapon;

    private StatusEffect statusEffect;

    private int strength;
    private int defense;
    private int speed;

    @Transient
    private List<PowerUp> activePowerUps = new ArrayList<>();

    private static final List<Weapon> WEAPONS = Arrays.asList(
            new Weapon(0, "Sword", 10, 0, 5),
            new Weapon(1, "Axe", 15, -5, 0),
            new Weapon(2, "Laser Gun", 5, 10, 2),
            new Weapon(3, "Hammer", 20, -10, 0)
    );

    public void assignRandomWeapon() {
        Random random = new Random();
        this.weapon = WEAPONS.get(random.nextInt(WEAPONS.size()));
    }

    public void applyPowerUp(PowerUp powerUp) {
        activePowerUps.add(powerUp);
    }

    public void removePowerUp(PowerUp powerUp) {
        activePowerUps.remove(powerUp);
    }
}
