package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.SearchPokemonsPort;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;

import java.util.List;

public final class SearchPokemons {

    private final SearchPokemonsPort searchPokemonsPort;

    public SearchPokemons(SearchPokemonsPort searchPokemonsPort) {
        this.searchPokemonsPort = searchPokemonsPort;
    }

    public List<PokemonDto> execute(SearchPokemonsQuery query) {
        PokemonCriteria criteria = new PokemonCriteria(query.sortBy());
        return searchPokemonsPort.find(criteria);
    }
}
