package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Optional<Player> getByName(String name);
}
