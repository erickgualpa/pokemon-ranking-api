package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;

import java.util.List;
import java.util.Objects;

public final class RankingDTO {
    public final List<PokemonDTO> pokemons;

    public RankingDTO(List<Pokemon> pokemons) {
        this.pokemons = pokemons.stream()
                .map(pokemon -> new PokemonDTO(pokemon.name()))
                .toList();
    }

    public List<PokemonDTO> pokemons() {
        return pokemons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankingDTO that = (RankingDTO) o;
        return Objects.equals(pokemons, that.pokemons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemons);
    }
}
