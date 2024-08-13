package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.PokemonSearchRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration.properties.clients.PokeApiClientProperties;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.searchpokemons.concurrent.PokemonSearchRepositoryConcurrentAdapter;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.searchpokemons.webflux.PokemonSearchRepositoryWebFluxAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@EnableConfigurationProperties(PokeApiClientProperties.class)
@Configuration
public class InfrastructureConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Pokemon Ranking API")
                );
    }

    @Primary
    @Bean
    public PokemonSearchRepository pokemonSearchRepositoryWebFluxAdapter(
            PokeApiClientProperties pokeApiClientProperties
    ) {
        return new PokemonSearchRepositoryWebFluxAdapter(pokeApiClientProperties);
    }

    @Bean
    public PokemonSearchRepository pokemonSearchRepositoryConcurrentAdapter(
            PokeApiClientProperties pokeApiClientProperties
    ) {
        return new PokemonSearchRepositoryConcurrentAdapter(pokeApiClientProperties);
    }
}
