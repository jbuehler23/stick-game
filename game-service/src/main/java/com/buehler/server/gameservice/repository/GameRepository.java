package com.buehler.server.gameservice.repository;

import com.buehler.server.gameservice.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {}
