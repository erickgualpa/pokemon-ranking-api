package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;

import java.util.List;

public final class FindPokemons {

    private final AggregateRepository<Pokemon> pokemonRepository;

    public FindPokemons(AggregateRepository<Pokemon> pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<PokemonDTO> execute(FindPokemonQuery query) {
        Criteria criteria = new PokemonCriteria(query.sortBy());
        return pokemonRepository.findMatching(criteria).stream()
                .map(p -> new PokemonDTO(p.getName()))
                .toList();
    }
}
