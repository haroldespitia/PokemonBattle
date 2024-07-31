package com.techicalTest.formula.monks.PokemonBattle.services;

import com.techicalTest.formula.monks.PokemonBattle.db.entities.DefenceItemsPerGame;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.DefensiveItem;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.FieldType;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.FieldTypeChart;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.Match;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.MatchMovement;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.Player;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.PlayerPokemon;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.PokemonCard;
import com.techicalTest.formula.monks.PokemonBattle.db.entities.TypeChart;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.DefenceItemsPerGameRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.DefensiveItemRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.FieldTypeChartRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.FieldTypeRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.MatchMovementRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.MatchRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.PlayerRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.PokemonCardRepository;
import com.techicalTest.formula.monks.PokemonBattle.db.repository.TypeChartRepository;
import com.techicalTest.formula.monks.PokemonBattle.dto.Movement;
import com.techicalTest.formula.monks.PokemonBattle.dto.NewMatchParameters;
import com.techicalTest.formula.monks.PokemonBattle.utils.GameStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonBattleService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final PokemonCardRepository pokemonCardRepository;
    private final FieldTypeRepository fieldTypeRepository;
    private final DefenceItemsPerGameRepository defenceItemsPerGameRepository;
    private final DefensiveItemRepository defensiveItemRepository;
    private final FieldTypeChartRepository fieldTypeChartRepository;
    private final TypeChartRepository typeChartRepository;
    private final MatchMovementRepository matchMovementRepository;
    private final Player computer;

    public PokemonBattleService(MatchRepository matchRepository, PlayerRepository playerRepository,
                                PokemonCardRepository pokemonCardRepository, FieldTypeRepository fieldTypeRepository,
                                DefenceItemsPerGameRepository defenceItemsPerGameRepository, DefensiveItemRepository defensiveItemRepository,
                                FieldTypeChartRepository fieldTypeChartRepository, TypeChartRepository typeChartRepository,
                                MatchMovementRepository matchMovementsRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.pokemonCardRepository = pokemonCardRepository;
        this.fieldTypeRepository = fieldTypeRepository;
        this.computer = playerRepository.getReferenceById(1);
        this.defenceItemsPerGameRepository = defenceItemsPerGameRepository;
        this.defensiveItemRepository = defensiveItemRepository;
        this.fieldTypeChartRepository = fieldTypeChartRepository;
        this.typeChartRepository = typeChartRepository;
        this.matchMovementRepository = matchMovementsRepository;
    }

    public Match startNewMatch(NewMatchParameters newMatchParameters) {
        Match newMatch = new Match();

        // Step 1. Create the new player
        Player player = createPlayer(newMatchParameters.getPlayerName());
        if (player.getId() == null) {
            playerRepository.save(player);
        }

        // Step 2. Generate the Pokemon's player
        List<PlayerPokemon> playerPokemon = generatePokemonList(newMatchParameters.getNumberOfPokemon(), player);

        // Step 3. Generate the Pokemon's computer
        List<PlayerPokemon> computerPokemon = generatePokemonList(newMatchParameters.getNumberOfPokemon(), computer);

        // Step 4. Generate the field type the match will be played
        FieldType fieldType = generateFieldType();

        // Step 5. Generate the defensive items in the match
        // Step 5.1. Items per Player
        List<DefenceItemsPerGame> defenceItemsPerGameListPlayer = generateDefensiveItemsPerGame(newMatchParameters.getNumberOfDefensiveItems());
        // Step 5.2. Items per computer
        List<DefenceItemsPerGame> defenceItemsPerGameListComputer = generateDefensiveItemsPerGame(newMatchParameters.getNumberOfDefensiveItems());

        // Step 6. Start the match
        newMatch.setStatus(GameStatus.CREATED.getName());
        newMatch.setPlayerPokemon(playerPokemon);
        newMatch.setComputerPokemon(computerPokemon);
        newMatch.setFieldType(fieldType);
        newMatch.setPlayerDefensiveItems(defenceItemsPerGameListPlayer);
        newMatch.setComputerDefensiveItems(defenceItemsPerGameListComputer);
        matchRepository.save(newMatch);

        return newMatch;
    }

    public Match executeMovement(Movement movement) {
        // Step 1. Retrieve Match information
        Match currentMatch = matchRepository.getReferenceById(movement.getMatchId());

        // Step 1. Check if the attack is possible
        if (!checkIfAttackIsPossible(currentMatch, movement.getPlayerSource())) {
            currentMatch.setStatus(GameStatus.COMPLETED.getName());
            matchRepository.save(currentMatch);
            return currentMatch;
        }

        List<PlayerPokemon> playerPokemon = currentMatch.getPlayerPokemon();
        List<PlayerPokemon> computerPokemon = currentMatch.getComputerPokemon();

        List<DefenceItemsPerGame> playerDefensiveItems = movement.getPlayerSource() == 0 ? currentMatch.getPlayerDefensiveItems() : currentMatch.getComputerDefensiveItems();

        // Step 2. Retrieve attacker and defence Pokemon
        PlayerPokemon sourcePokemon = findPlayerPokemonInList(movement.getPlayerSource() == 0 ? playerPokemon : computerPokemon, movement.getSourcePokemon());
        PlayerPokemon destinyPokemon = findPlayerPokemonInList(movement.getPlayerSource() == 1 ? playerPokemon : computerPokemon, movement.getSourcePokemon());
        DefenceItemsPerGame defensiveItem = null;
        if (movement.getDefensiveItem() != -1) {
            defensiveItem = findDefensiveItemInList(playerDefensiveItems, movement.getDefensiveItem());
            defensiveItem.setStatus(Boolean.FALSE);
        }

        Float fieldBonus = getFieldBonus(currentMatch.getFieldType(), sourcePokemon.getPokemonCard());
        Float typeBonus = getTypeBonus(sourcePokemon.getPokemonCard());
        Float defensiveItemBonus = getDefensiveItemBonus(sourcePokemon, movement.getDefensiveItem());

        // Step 4. Calculate the total attack amount
        Float totalBonus = fieldBonus + typeBonus;
        Float totalMovement;

        // Step 6. Update the healValue in PlayerPokemon
        if (movement.getDefensiveItem() == -1) {
            totalMovement = sourcePokemon.getPokemonCard().getAttackValue() + totalBonus;
            destinyPokemon.setCurrentHealth(destinyPokemon.getCurrentHealth() - totalMovement);
        } else {
            totalMovement = defensiveItemBonus;
            sourcePokemon.setCurrentHealth(sourcePokemon.getCurrentHealth() + totalMovement);
        }

        // Step 5. Update the movement in MatchMovement
        MatchMovement matchMovement = MatchMovement.builder()
                .match(currentMatch)
                .source(sourcePokemon)
                .destiny(destinyPokemon)
                .finalAttackAmount(totalMovement)
                .build();
        matchMovementRepository.save(matchMovement);

        // Step 7. Return updated Match
        updateMatchValues(currentMatch, sourcePokemon, destinyPokemon, defensiveItem, movement.getPlayerSource());
        matchRepository.save(currentMatch);

        return currentMatch;
    }

    public Boolean finishMatch() {

        // Step 1. Retrieve the Match
        // Step 2. Check all Pokemon status
        // Step 3. Close the game
        // Step 4. Proclaim the winner
        // Step 5. Return the response

        return Boolean.TRUE;
    }

    private Player createPlayer(String playerName) {
        Player player = playerRepository.getByName(playerName).orElse(null);
        if (player == null) {
            return new Player(null, playerName, 0, 0);
        }
        return player;
    }

    private List<PlayerPokemon> generatePokemonList(Integer numberOfPokemon, Player player) {
        List<PlayerPokemon> pokemonList = new ArrayList<>();
        List<PokemonCard> pokemonCardList = pokemonCardRepository.getPokemonListRandomly(numberOfPokemon);
        pokemonCardList.forEach(i -> {
            PlayerPokemon playerPokemon = new PlayerPokemon();
            playerPokemon.setPokemonCard(i);
            playerPokemon.setPlayer(player);
            playerPokemon.setCurrentHealth(i.getDefaultHealth());

            pokemonList.add(playerPokemon);
        });

        return pokemonList;
    }

    private FieldType generateFieldType() {
        return fieldTypeRepository.getFieldRandomly();
    }

    private List<DefenceItemsPerGame> generateDefensiveItemsPerGame(Integer numberOfDefensiveItems) {
        List<DefenceItemsPerGame> defenceItemsPerGameList = new ArrayList<>();
        List<DefensiveItem> defensiveItems = defensiveItemRepository.getDefensiveItemsRandomly(numberOfDefensiveItems);
        defensiveItems.forEach(i -> {
            DefenceItemsPerGame defenceItemsPerGame = new DefenceItemsPerGame();
            defenceItemsPerGame.setDefensiveItem(i);
            defenceItemsPerGame.setStatus(Boolean.TRUE);
        });
        return defenceItemsPerGameList;
    }

    private PlayerPokemon findPlayerPokemonInList(List<PlayerPokemon> playerPokemon, int pokemonId) {
        return playerPokemon.stream().parallel().filter(i -> i.getId() == pokemonId).findFirst().orElse(null);
    }
    
    private DefenceItemsPerGame findDefensiveItemInList(List<DefenceItemsPerGame> playerDefensiveItems, int defensiveItemId) {
        return playerDefensiveItems.stream().parallel().filter(i -> i.getId() == defensiveItemId).findFirst().orElse(null);
    }

    private List<FieldTypeChart> findFieldTypeChard(Integer id) {
        return fieldTypeChartRepository.findByTypeId(id);
    }

    private Float getFieldBonus(FieldType fieldType, PokemonCard pokemonCard) {
        List<FieldTypeChart> fieldTypeCharts = findFieldTypeChard(fieldType.getId());
        FieldTypeChart aux = fieldTypeCharts.stream().filter(i -> i.getType().getId().equals(pokemonCard.getType().getId())).findFirst().orElse(null);
        if (aux != null) {
            return pokemonCard.getAttackValue() * (fieldType.getBonus() / 100);
        }
        return 0F;
    }

    private Float getTypeBonus(PokemonCard pokemonCard) {
        List<TypeChart> typeCharts = typeChartRepository.findByTypeId(pokemonCard.getType().getId());
        TypeChart aux = typeCharts.stream().filter(i -> i.getTypeStrongTo().getId().equals(pokemonCard.getType().getId())).findFirst().orElse(null);
        if (aux != null) {
            return pokemonCard.getAttackValue() * (pokemonCard.getType().getBonus() / 100);
        }
        return 0F;
    }

    private Float getDefensiveItemBonus(PlayerPokemon playerPokemon, Integer defensiveItem) {
        DefensiveItem item = defensiveItemRepository.findById(defensiveItem).orElse(null);
        if (item != null) {
            return playerPokemon.getPokemonCard().getAttackValue() * (item.getHealingValue() / 100);
        }
        return 0F;
    }

    private void updateMatchValues(Match currentMatch, PlayerPokemon sourcePokemon, PlayerPokemon destinyPokemon,
                                   DefenceItemsPerGame defensiveItem, Integer playerSource) {
        // Step 1. Update player Pokemon
        List<PlayerPokemon> playerPokemon = playerSource == 0 ? currentMatch.getPlayerPokemon() : currentMatch.getComputerPokemon();
        for (int i = 0; i < playerPokemon.size(); i++) {
            if (playerPokemon.get(i).getId().equals(sourcePokemon.getId())) {
                playerPokemon.remove(playerPokemon.get(i));
                playerPokemon.add(sourcePokemon);
                break;
            }
        }

        // Step 2. Update computer Pokemon
        List<PlayerPokemon> computerPokemon = playerSource == 1 ? currentMatch.getPlayerPokemon() : currentMatch.getComputerPokemon();
        for (int i = 0; i < computerPokemon.size(); i++) {
            if (computerPokemon.get(i).getId().equals(destinyPokemon.getId())) {
                computerPokemon.remove(computerPokemon.get(i));
                computerPokemon.add(destinyPokemon);
                break;
            }
        }

        // Step 3. Update DefenseItemsPerGame
        List<DefenceItemsPerGame> defenceItemsPerGames = playerSource == 0 ? currentMatch.getPlayerDefensiveItems() : currentMatch.getComputerDefensiveItems();
        for (int i = 0; i < defenceItemsPerGames.size(); i++) {
            if (defenceItemsPerGames.get(i).getId().equals(defensiveItem.getId())) {
                defenceItemsPerGames.remove(defenceItemsPerGames.get(i));
                defenceItemsPerGames.add(defensiveItem);
                break;
            }
        }

        // Step 4. Set all new values
        currentMatch.setPlayerPokemon(playerPokemon);
        currentMatch.setComputerPokemon(computerPokemon);
        if (playerSource == 0) {
            currentMatch.setPlayerDefensiveItems(defenceItemsPerGames);
        } else {
            currentMatch.setComputerDefensiveItems(defenceItemsPerGames);
        }
    }

    private boolean checkIfAttackIsPossible(Match currentMatch, Integer playerSource) {
        List<PlayerPokemon> playerPokemonList = playerSource == 0 ? currentMatch.getPlayerPokemon() : currentMatch.getComputerPokemon();
        for (PlayerPokemon playerPokemon : playerPokemonList) {
            if (playerPokemon.getCurrentHealth() > 0) {
                return true;
            }
        }
        return false;
    }
}
