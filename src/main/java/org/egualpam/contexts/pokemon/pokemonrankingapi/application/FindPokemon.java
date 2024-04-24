package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonEntity;

import java.util.List;

public class FindPokemon {

    private final AggregateRepository<PokemonEntity> pokemonRepository;

    public FindPokemon(AggregateRepository<PokemonEntity> pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<PokemonDTO> execute(FindPokemonQuery query) {
        Criteria criteria = new PokemonCriteria(query.sortBy());
        return pokemonRepository.findMatching(criteria).stream()
                .map(p -> new PokemonDTO(p.getName()))
                .toList();
    }
}
