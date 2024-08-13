package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.webflux;

import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration.properties.clients.PokeApiClientProperties;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared.ExternalPokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared.GetPokemonsResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;

public final class WebfluxPokemonsSupplier implements Supplier<List<ExternalPokemonDto>> {

    private final PokeApiClientProperties pokeApiClientProperties;
    private final WebClient webClient;

    public WebfluxPokemonsSupplier(PokeApiClientProperties pokeApiClientProperties) {
        this.webClient = WebClient
                .builder()
                .exchangeStrategies(
                        ExchangeStrategies
                                .builder()
                                .codecs(codecs -> codecs
                                        .defaultCodecs()
                                        .maxInMemorySize(10 * 1024 * 1024))
                                .build())
                .build();
        this.pokeApiClientProperties = pokeApiClientProperties;
    }

    @Override
    public List<ExternalPokemonDto> get() {
        GetPokemonsResponse pokemons =
                webClient
                        .get()
                        .uri(pokeApiClientProperties.host() + pokeApiClientProperties.getPokemonsPath())
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
                .map(r -> new ExternalPokemonDto(r.name(), r.weight(), r.height(), r.baseExperience()))
                .collectList()
                .block();
    }
}
