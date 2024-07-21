package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.controllers;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDto;

import java.util.List;

public final class GetPokemonRankingResponse {

    private final List<Pokemon> ranking;

    public GetPokemonRankingResponse(List<PokemonDto> pokemons) {
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
