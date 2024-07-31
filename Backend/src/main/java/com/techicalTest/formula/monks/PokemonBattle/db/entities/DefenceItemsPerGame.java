package com.techicalTest.formula.monks.PokemonBattle.db.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DEFENCE_ITEMS_PER_GAME")
public class DefenceItemsPerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "defensive_items_per_game_id", referencedColumnName = "id", nullable = false)
    private Match match;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "defensive_item_per_game_id", referencedColumnName = "id", nullable = false)
    private DefensiveItem defensiveItem;

    private Boolean status;

}
