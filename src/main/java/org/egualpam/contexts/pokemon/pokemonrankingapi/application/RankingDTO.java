package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import java.util.List;

public record RankingDTO(List<Pokemon> pokemons) {
    public record Pokemon(String name) {
    }
}
