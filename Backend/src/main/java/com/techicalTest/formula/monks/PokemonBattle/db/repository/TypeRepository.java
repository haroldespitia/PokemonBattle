package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
}
