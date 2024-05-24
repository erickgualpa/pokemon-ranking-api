package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.PokemonRepositoryFacade;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.concurrent.ConcurrentPokemonsSupplier;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration.clients.properties.PokeApiClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public Supplier<List<PokemonDTO>> pokemonsSupplier(
            @Value("${clients.poke-api.host}") String pokeApiHost,
            @Value("${clients.poke-api.get-pokemons.path}") String getPokemonsPath,
            PokeApiClientProperties pokeApiClientProperties
    ) {
        return new ConcurrentPokemonsSupplier(pokeApiClientProperties);
    }

    @Bean
    public AggregateRepository<Pokemon> pokemonRepository(Supplier<List<PokemonDTO>> pokemonsSupplier) {
        return new PokemonRepositoryFacade(pokemonsSupplier);
    }
}
