package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

public enum SortBy {
    HEIGHT,
    WEIGHT,
    BASE_EXPERIENCE;

    public static SortBy fromString(String value) {
        try {
            return SortBy.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // TODO: Create a custom exception
            throw new RuntimeException("Invalid sorting criteria");
        }
    }
}
