package org.egualpam.services.pokemon.pokemonrankingapi.application;

import java.util.List;

public record RankingDTO(List<Pokemon> pokemons) {
    public record Pokemon(String name) {
    }
}
