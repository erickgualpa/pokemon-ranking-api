package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.InvalidUniqueId;

import java.util.UUID;

public record UniqueId(String value) {
    public UniqueId {
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            throw new InvalidUniqueId(e);
        }
    }
}
