package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.SearchPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.PokemonSearchRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public SearchPokemons searchPokemons(
            PokemonSearchRepository pokemonSearchRepository
    ) {
        return new SearchPokemons(pokemonSearchRepository);
    }
}
