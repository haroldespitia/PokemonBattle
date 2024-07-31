package com.techicalTest.formula.monks.PokemonBattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    private Integer matchId;
    private Integer defensiveItem;
    private Integer playerSource;
    private Integer sourcePokemon;
    private Integer destinyPokemon;
}
