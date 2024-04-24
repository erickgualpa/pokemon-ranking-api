package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.List;

public interface AggregateRepository<T extends AggregateRoot> {
    T find(Criteria criteria);

    List<T> findMatching(Criteria criteria);
}
