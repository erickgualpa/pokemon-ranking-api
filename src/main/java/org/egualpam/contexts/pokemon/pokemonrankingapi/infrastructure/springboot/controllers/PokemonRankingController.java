package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.controllers;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.SearchPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.SearchPokemonsQuery;
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

    private final SearchPokemons searchPokemons;

    public PokemonRankingController(SearchPokemons searchPokemons) {
        this.searchPokemons = searchPokemons;
    }

    @GetMapping("/heaviest")
    public ResponseEntity<GetPokemonRankingResponse> getHeaviestPokemons() {
        List<PokemonDto> pokemons;
        try {
            pokemons = new ArrayList<>(searchPokemons.execute(new SearchPokemonsQuery("weight")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }

    @GetMapping("/highest")
    public ResponseEntity<GetPokemonRankingResponse> getHighestPokemons() {
        List<PokemonDto> pokemons;
        try {
            pokemons = new ArrayList<>(searchPokemons.execute(new SearchPokemonsQuery("height")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }

    @GetMapping("/most-experienced")
    public ResponseEntity<GetPokemonRankingResponse> getMostExperiencedPokemons() {
        List<PokemonDto> pokemons;
        try {
            pokemons = new ArrayList<>(searchPokemons.execute(new SearchPokemonsQuery("base_experience")));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(new GetPokemonRankingResponse(pokemons));
    }
}
