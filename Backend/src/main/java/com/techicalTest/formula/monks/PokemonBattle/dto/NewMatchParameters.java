package com.techicalTest.formula.monks.PokemonBattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMatchParameters {
    private Integer numberOfPokemon;
    private String playerName;
    private Integer numberOfDefensiveItems;
}
