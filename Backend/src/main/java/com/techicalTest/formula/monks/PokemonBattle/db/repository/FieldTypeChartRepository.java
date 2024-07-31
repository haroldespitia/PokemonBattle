package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.FieldTypeChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldTypeChartRepository extends JpaRepository<FieldTypeChart, Integer> {

    List<FieldTypeChart> findByTypeId(int fieldTypeId);
}
