package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetPokemonDetailsResponse(
        String name,
        Integer weight,
        Integer height,
        @JsonProperty("base_experience")
        Integer baseExperience
) {
}
