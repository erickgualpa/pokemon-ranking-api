package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import static java.util.UUID.randomUUID;

public final class Pokemon implements AggregateRoot {

    private final AggregateId id;
    private final String name;

    public Pokemon(String name) {
        this.id = new AggregateId(randomUUID().toString());
        this.name = name;
    }

    @Override
    public AggregateId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
