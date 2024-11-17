package com.buehler.server.gameservice.repository;

import com.buehler.server.gameservice.entities.PowerUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerUpRepository extends JpaRepository<PowerUp, Long> {
}
