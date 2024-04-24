package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.Optional;

import static java.util.Objects.nonNull;

public final class PokemonCriteria implements Criteria {

    private static final int DEFAULT_LIMIT = 5;

    private final SortBy sortBy;
    private final Limit limit;

    public PokemonCriteria(String type) {
        this.sortBy = nonNull(type) ? SortBy.fromString(type) : null;
        this.limit = new Limit(DEFAULT_LIMIT);
    }

    public Optional<SortBy> getType() {
        return Optional.of(sortBy);
    }

    public Optional<Limit> getLimit() {
        return Optional.of(limit);
    }
}
