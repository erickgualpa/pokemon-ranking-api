package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public FindPokemons findPokemon(AggregateRepository<Pokemon> pokemonRepository) {
        return new FindPokemons(pokemonRepository);
    }
}
