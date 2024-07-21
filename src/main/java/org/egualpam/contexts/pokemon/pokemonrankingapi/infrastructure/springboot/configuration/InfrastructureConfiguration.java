package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.SearchPokemonsPort;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.SearchPokemonsFacadeAdapter;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.ExternalPokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.concurrent.ConcurrentPokemonsSupplier;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.webflux.WebfluxPokemonsSupplier;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration.properties.clients.PokeApiClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.function.Supplier;

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

    @Bean
    public Supplier<List<ExternalPokemonDto>> concurrentPokemonsSupplier(
            PokeApiClientProperties pokeApiClientProperties
    ) {
        return new ConcurrentPokemonsSupplier(pokeApiClientProperties);
    }

    @Primary
    @Bean
    public Supplier<List<ExternalPokemonDto>> webfluxPokemonsSupplier(PokeApiClientProperties pokeApiClientProperties) {
        return new WebfluxPokemonsSupplier(pokeApiClientProperties);
    }

    @Bean
    public SearchPokemonsPort searchPokemonsPort(Supplier<List<ExternalPokemonDto>> pokemonsSupplier) {
        return new SearchPokemonsFacadeAdapter(pokemonsSupplier);
    }
}
