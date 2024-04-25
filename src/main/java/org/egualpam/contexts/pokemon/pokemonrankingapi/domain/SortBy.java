package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.InvalidSortingMethod;

public enum SortBy {
    HEIGHT,
    WEIGHT,
    BASE_EXPERIENCE;

    public static SortBy fromString(String value) {
        try {
            return SortBy.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSortingMethod();
        }
    }
}
