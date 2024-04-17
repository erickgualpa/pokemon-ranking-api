package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.controllers;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.RankingDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.RetrieveRanking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.RetrieveRankingQuery;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon-ranking")
public class PokemonRankingController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonRankingController.class);

    private static final int DEFAULT_RANKING_LIMIT = 5;

    private final RetrieveRanking retrieveRanking;

    public PokemonRankingController(RetrieveRanking retrieveRanking) {
        this.retrieveRanking = retrieveRanking;
    }

    @GetMapping("/heaviest")
    public ResponseEntity<GetPokemonRankingResponse> getHeaviestPokemons() {
        RankingDTO rankingDTO;
        try {
            rankingDTO = retrieveRanking.execute(
                    new RetrieveRankingQuery(RankingId.HEAVIEST.name(), DEFAULT_RANKING_LIMIT)
            );
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(
                new GetPokemonRankingResponse(
                        rankingDTO.pokemons().stream()
                                .map(p -> new GetPokemonRankingResponse.Pokemon(p.name()))
                                .toList())
        );
    }

    @GetMapping("/highest")
    public ResponseEntity<GetPokemonRankingResponse> getHighestPokemons() {
        RankingDTO rankingDTO;
        try {
            rankingDTO = retrieveRanking.execute(
                    new RetrieveRankingQuery(RankingId.HIGHEST.name(), DEFAULT_RANKING_LIMIT)
            );
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(
                new GetPokemonRankingResponse(
                        rankingDTO.pokemons().stream()
                                .map(p -> new GetPokemonRankingResponse.Pokemon(p.name()))
                                .toList())
        );
    }

    @GetMapping("/most-experienced")
    public ResponseEntity<GetPokemonRankingResponse> getMostExperiencedPokemons() {
        RankingDTO rankingDTO;
        try {
            rankingDTO = retrieveRanking.execute(
                    new RetrieveRankingQuery(RankingId.MOST_EXPERIENCED.name(), DEFAULT_RANKING_LIMIT)
            );
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(
                new GetPokemonRankingResponse(
                        rankingDTO.pokemons().stream()
                                .map(p -> new GetPokemonRankingResponse.Pokemon(p.name()))
                                .toList())
        );
    }

    public record GetPokemonRankingResponse(List<Pokemon> ranking) {
        record Pokemon(String name) {
        }
    }
}
