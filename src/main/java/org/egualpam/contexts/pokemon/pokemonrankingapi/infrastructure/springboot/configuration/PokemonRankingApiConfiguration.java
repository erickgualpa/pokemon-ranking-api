package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.GetPokemonsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.PokemonRepositoryFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
    public Supplier<List<PokemonDTO>> pokemonDataSupplier(
            WebClient webClient,
            @Value("${clients.poke-api.host}") String pokeApiHost,
            @Value("${clients.poke-api.get-pokemons.path}") String getPokemonsPath
    ) {
        return () -> {
            GetPokemonsResponse pokemons =
                    webClient
                            .get()
                            .uri(pokeApiHost + getPokemonsPath)
                            .retrieve()
                            .bodyToMono(GetPokemonsResponse.class)
                            .block();

            // TODO: Address NPE warning
            return Flux.fromIterable(pokemons.results())
                    .flatMap(pokemon ->
                            webClient
                                    .get()
                                    .uri(pokemon.url())
                                    .retrieve()
                                    .bodyToMono(GetPokemonDetailsResponse.class))
                    .map(r -> new PokemonDTO(r.name(), r.weight(), r.height(), r.baseExperience()))
                    .collectList()
                    .block();
        };
    }

    @Bean
    public AggregateRepository<Pokemon> pokemonRepository(Supplier<List<PokemonDTO>> pokemonDataSupplier) {
        return new PokemonRepositoryFacade(pokemonDataSupplier);
    }

    @Bean
    public FindPokemons findPokemon(AggregateRepository<Pokemon> pokemonRepository) {
        return new FindPokemons(pokemonRepository);
    }
}
