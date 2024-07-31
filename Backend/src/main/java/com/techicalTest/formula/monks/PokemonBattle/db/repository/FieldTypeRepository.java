package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTypeRepository extends JpaRepository<FieldType, Integer> {

    @Query(value = "SELECT ft FROM FieldType ft ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    FieldType getFieldRandomly();
}
