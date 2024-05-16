package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.webflux;

import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonsResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;

public final class WebfluxPokemonsSupplier implements Supplier<List<PokemonDTO>> {

    private final WebClient webClient;
    private final String pokeApiHost;
    private final String getPokemonsPath;

    public WebfluxPokemonsSupplier(String pokeApiHost, String getPokemonsPath) {
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
        this.pokeApiHost = pokeApiHost;
        this.getPokemonsPath = getPokemonsPath;
    }

    @Override
    public List<PokemonDTO> get() {
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
    }
}
