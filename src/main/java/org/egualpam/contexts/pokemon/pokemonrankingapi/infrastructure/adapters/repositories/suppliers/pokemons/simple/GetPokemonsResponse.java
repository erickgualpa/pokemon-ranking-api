package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.simple;

import java.util.List;

record GetPokemonsResponse(List<Pokemon> results) {
    record Pokemon(String name, String url) {
    }
}