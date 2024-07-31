package com.techicalTest.formula.monks.PokemonBattle.controllers;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.Match;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.Player;
import com.techicalTest.formula.monks.PokemonBattle.dto.Movement;
import com.techicalTest.formula.monks.PokemonBattle.dto.NewMatchParameters;
import com.techicalTest.formula.monks.PokemonBattle.services.PokemonBattleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController("/pokemon_battle")
public class PokemonBattleController {

    private final Logger LOGGER = Logger.getLogger(PokemonBattleController.class.getName());
    private final PokemonBattleService pokemonBattleService;

    public PokemonBattleController(PokemonBattleService pokemonBattleService) {
        this.pokemonBattleService = pokemonBattleService;
    }

    @GetMapping("/create-match")
    public Match startMatch(@RequestBody NewMatchParameters newMatchParameters) {
        LOGGER.info("Starting the request processing");
        Match newMatch = pokemonBattleService.startNewMatch(newMatchParameters);
        LOGGER.info("Starting the request processing");

        return newMatch;
    }

    @PostMapping("/execute-turn")
    public void executeTurn(Movement movement) {
    }

    @PostMapping("/create-player")
    public Player createPlayer() {
        return null;
    }


}
