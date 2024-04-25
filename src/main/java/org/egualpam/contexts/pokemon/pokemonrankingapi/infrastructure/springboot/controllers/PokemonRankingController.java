package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.controllers;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemonQuery;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon-ranking")
public final class PokemonRankingController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonRankingController.class);

    private final FindPokemons findPokemons;

    public PokemonRankingController(FindPokemons findPokemons) {
        this.findPokemons = findPokemons;
    }

    @GetMapping("/heaviest")
    public ResponseEntity<GetPokemonRankingResponse> getHeaviestPokemons() {
        List<PokemonDTO> pokemons;
        try {
            pokemons = new ArrayList<>(findPokemons.execute(new FindPokemonQuery("weight")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }

    @GetMapping("/highest")
    public ResponseEntity<GetPokemonRankingResponse> getHighestPokemons() {
        List<PokemonDTO> pokemons;
        try {
            pokemons = new ArrayList<>(findPokemons.execute(new FindPokemonQuery("height")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }

    @GetMapping("/most-experienced")
    public ResponseEntity<GetPokemonRankingResponse> getMostExperiencedPokemons() {
        List<PokemonDTO> pokemons;
        try {
            pokemons = new ArrayList<>(findPokemons.execute(new FindPokemonQuery("base_experience")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }
}
