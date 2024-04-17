package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.ArrayList;
import java.util.List;

public final class Ranking {
    private final RankingId id;
    private final List<Pokemon> pokemons = new ArrayList<>();

    public Ranking(RankingId id) {
        this.id = id;
    }

    public void addPokemon(String pokemonName) {
        pokemons.add(new Pokemon(pokemonName));
    }

    public List<Pokemon> getPokemons() {
        // TODO: Return a deep copy of the original list
        return pokemons;
    }
}
