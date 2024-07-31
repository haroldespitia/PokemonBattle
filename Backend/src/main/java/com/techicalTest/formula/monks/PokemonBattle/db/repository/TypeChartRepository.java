package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.TypeChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeChartRepository extends JpaRepository<TypeChart, Integer> {

    List<TypeChart> findByTypeId(Integer typeId);
}
