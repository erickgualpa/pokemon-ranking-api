package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.PokemonRepositoryFacade;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.simple.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.simple.SimplePokemonsSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class PokemonRankingApiConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Pokemon Ranking API")
                );
    }

    @Bean
    public WebClient defaultWebClient() {
        return WebClient
                .builder()
                .exchangeStrategies(
                        ExchangeStrategies
                                .builder()
                                .codecs(codecs -> codecs
                                        .defaultCodecs()
                                        .maxInMemorySize(10 * 1024 * 1024))
                                .build())
                .build();
    }

    @Bean
    public Supplier<List<PokemonDTO>> pokemonsSupplier(
            WebClient webClient,
            @Value("${clients.poke-api.host}") String pokeApiHost,
            @Value("${clients.poke-api.get-pokemons.path}") String getPokemonsPath
    ) {
        return new SimplePokemonsSupplier(webClient, pokeApiHost, getPokemonsPath);
    }

    @Bean
    public AggregateRepository<Pokemon> pokemonRepository(Supplier<List<PokemonDTO>> pokemonsSupplier) {
        return new PokemonRepositoryFacade(pokemonsSupplier);
    }

    @Bean
    public FindPokemons findPokemon(AggregateRepository<Pokemon> pokemonRepository) {
        return new FindPokemons(pokemonRepository);
    }
}
