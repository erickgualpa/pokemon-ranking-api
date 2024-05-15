package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.simple;

import com.fasterxml.jackson.annotation.JsonProperty;

record GetPokemonDetailsResponse(
        String name,
        Integer weight,
        Integer height,
        @JsonProperty("base_experience")
        Integer baseExperience
) {
}
