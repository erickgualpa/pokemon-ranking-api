package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import static java.util.UUID.randomUUID;

// TODO: Rename this into just 'Pokemon'
public class PokemonEntity implements AggregateRoot {

    private final AggregateId id;
    private final String name;

    public PokemonEntity(String name) {
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
