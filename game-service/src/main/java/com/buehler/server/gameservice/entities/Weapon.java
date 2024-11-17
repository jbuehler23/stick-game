package com.buehler.server.gameservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weapon {

    @Id
    private Integer id;
    private String name;       // e.g., "Sword", "Axe", "Laser Gun"
    private int strengthBonus; // Extra strength this weapon gives
    private int speedBonus;    // Extra speed this weapon gives
    private int defenseBonus;  // Extra defense this weapon gives

}
