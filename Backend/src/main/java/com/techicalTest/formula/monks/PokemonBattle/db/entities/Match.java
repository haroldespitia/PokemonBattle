package com.techicalTest.formula.monks.PokemonBattle.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MATCH")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private FieldType fieldType;

    @OneToMany(mappedBy = "match")
    private List<DefenceItemsPerGame> playerDefensiveItems;

    @OneToMany(mappedBy = "match")
    private List<DefenceItemsPerGame> computerDefensiveItems;

    @OneToMany(mappedBy = "match")
    private List<PlayerPokemon> playerPokemon;

    @OneToMany(mappedBy = "match")
    private List<PlayerPokemon> computerPokemon;

    private String status;
}
