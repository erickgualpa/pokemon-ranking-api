package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.Optional;

import static java.util.Objects.nonNull;

public final class RankingCriteria implements Criteria {
    private final RankingType type;
    private final RankingLimit limit;

    public RankingCriteria(String type, Integer limit) {
        this.type = nonNull(type) ? RankingType.valueOf(type) : null;
        this.limit = nonNull(limit) ? new RankingLimit(limit) : null;
    }

    public Optional<RankingType> getType() {
        return Optional.of(type);
    }

    public Optional<RankingLimit> getLimit() {
        return Optional.of(limit);
    }
}
