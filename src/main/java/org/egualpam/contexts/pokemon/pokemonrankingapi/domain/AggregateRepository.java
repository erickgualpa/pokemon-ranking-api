package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

public interface AggregateRepository<T extends AggregateRoot> {
    T find(Criteria criteria);
}
