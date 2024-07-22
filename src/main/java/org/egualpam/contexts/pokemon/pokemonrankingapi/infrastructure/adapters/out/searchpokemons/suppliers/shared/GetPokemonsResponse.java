package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.searchpokemons.suppliers.shared;

import java.util.List;

public record GetPokemonsResponse(List<Pokemon> results) {
    public record Pokemon(String name, String url) {
    }
}