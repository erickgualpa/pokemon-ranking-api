package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.PokemonSearchRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;

import java.util.List;

public final class SearchPokemons {

    private final PokemonSearchRepository pokemonSearchRepository;

    public SearchPokemons(PokemonSearchRepository pokemonSearchRepository) {
        this.pokemonSearchRepository = pokemonSearchRepository;
    }

    public List<PokemonDto> execute(SearchPokemonsQuery query) {
        PokemonCriteria criteria = new PokemonCriteria(query.sortBy());
        return pokemonSearchRepository.find(criteria);
    }
}
