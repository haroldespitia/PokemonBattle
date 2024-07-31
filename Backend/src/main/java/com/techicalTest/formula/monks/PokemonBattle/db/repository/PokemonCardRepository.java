package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.PokemonCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonCardRepository extends JpaRepository<PokemonCard, Integer> {

    @Query(value = "SELECT pc FROM PokemonCard pc ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<PokemonCard> getPokemonListRandomly(int limit);
}
