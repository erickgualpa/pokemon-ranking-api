package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

public final class Ranking implements AggregateRoot {
    private final AggregateId id;
    private final RankingType type;
    private final List<Pokemon> pokemons = new ArrayList<>();

    public Ranking(RankingType type) {
        this.id = new AggregateId(randomUUID().toString());
        this.type = type;
    }

    @Override
    public AggregateId getId() {
        return id;
    }

    public void addPokemon(String pokemonName) {
        pokemons.add(new Pokemon(pokemonName));
    }

    public List<Pokemon> getPokemons() {
        return pokemons.stream()
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

}
