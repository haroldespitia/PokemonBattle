package com.techicalTest.formula.monks.PokemonBattle.utils;

public enum GameStatus {
    PLAYING("PLAYING"), CREATED("CREATED"), COMPLETED("COMPLETED");

    private final String name;

    GameStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
