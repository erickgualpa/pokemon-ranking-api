package org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;

import java.util.List;

public interface SearchPokemonsPort {
    List<PokemonDto> find(PokemonCriteria pokemonCriteria);
}
