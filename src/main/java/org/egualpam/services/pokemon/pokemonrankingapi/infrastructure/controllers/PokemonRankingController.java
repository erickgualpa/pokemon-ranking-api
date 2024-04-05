package org.egualpam.services.pokemon.pokemonrankingapi.infrastructure.controllers;

import org.egualpam.services.pokemon.pokemonrankingapi.application.RankingDTO;
import org.egualpam.services.pokemon.pokemonrankingapi.application.RetrieveRanking;
import org.egualpam.services.pokemon.pokemonrankingapi.application.RetrieveRankingQuery;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingId;
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
    private final RetrieveRanking retrieveRanking;

    public PokemonRankingController(RetrieveRanking retrieveRanking) {
        this.retrieveRanking = retrieveRanking;
    }

    @GetMapping("/heaviest")
    public ResponseEntity<GetHeaviestPokemonsRankingResponse> getHeaviestPokemons() {
        RankingDTO rankingDTO;
        try {
            rankingDTO = retrieveRanking.execute(new RetrieveRankingQuery(RankingId.HEAVIEST.name(), 5));
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ranking", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(
                new GetHeaviestPokemonsRankingResponse(
                        rankingDTO.pokemons().stream()
                                .map(p -> new GetHeaviestPokemonsRankingResponse.Pokemon(p.name()))
                                .toList())
        );
    }

    public record GetHeaviestPokemonsRankingResponse(List<Pokemon> ranking) {
        record Pokemon(String name) {
        }
    }
}
