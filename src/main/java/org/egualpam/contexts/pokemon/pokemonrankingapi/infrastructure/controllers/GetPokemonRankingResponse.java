package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.controllers;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDTO;

import java.util.List;

public final class GetPokemonRankingResponse {

    private final List<Pokemon> ranking;

    public GetPokemonRankingResponse(List<PokemonDTO> pokemons) {
        this.ranking = pokemons.stream()
                .map(p -> new GetPokemonRankingResponse.Pokemon(p.name()))
                .toList();
    }

    public List<Pokemon> getRanking() {
        return ranking;
    }

    public record Pokemon(String name) {
    }
}
