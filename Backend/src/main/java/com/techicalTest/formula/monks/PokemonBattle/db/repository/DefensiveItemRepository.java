package com.techicalTest.formula.monks.PokemonBattle.db.repository;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.DefensiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefensiveItemRepository extends JpaRepository<DefensiveItem, Integer> {

    @Query(value = "SELECT di FROM DefensiveItem di ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<DefensiveItem> getDefensiveItemsRandomly(int limit);

}
