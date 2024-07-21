package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.SearchPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.SearchPokemonsPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public SearchPokemons searchPokemons(
            SearchPokemonsPort searchPokemonsPort
    ) {
        return new SearchPokemons(searchPokemonsPort);
    }
}
