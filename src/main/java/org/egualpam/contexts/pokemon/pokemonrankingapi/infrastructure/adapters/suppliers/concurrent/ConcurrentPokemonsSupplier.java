package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.concurrent;

import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.ExternalPokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.shared.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.suppliers.shared.GetPokemonsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration.properties.clients.PokeApiClientProperties;
import org.slf4j.Logger;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

public final class ConcurrentPokemonsSupplier implements Supplier<List<ExternalPokemonDto>> {

    private static final int THREAD_POOL_MAX_SIZE = 50;
    private final Logger logger = getLogger(ConcurrentPokemonsSupplier.class);

    private final RestClient restClient;
    private final PokeApiClientProperties pokeApiClientProperties;

    public ConcurrentPokemonsSupplier(PokeApiClientProperties pokeApiClientProperties) {
        this.restClient = RestClient.create();
        this.pokeApiClientProperties = pokeApiClientProperties;
    }

    @Override
    public List<ExternalPokemonDto> get() {
        List<GetPokemonsResponse.Pokemon> servicePokemons =
                restClient.get()
                        .uri(pokeApiClientProperties.host() + pokeApiClientProperties.getPokemonsPath())
                        .retrieve()
                        .body(GetPokemonsResponse.class)
                        // TODO: Address NPE warning
                        .results();

        List<ExternalPokemonDto> pokemons = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_MAX_SIZE);

        Instant start = Instant.now();
        servicePokemons.forEach(p -> executorService.submit(() -> getPokemonDetails(p, pokemons)));
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("Error waiting for executor service termination", e);
        }
        logger.info("Pokemons retrieved in: {}", Duration.between(start, Instant.now()).toMillis());

        return pokemons;
    }

    private void getPokemonDetails(GetPokemonsResponse.Pokemon p, List<ExternalPokemonDto> pokemonsResult) {
        GetPokemonDetailsResponse pokemonDetails =
                restClient.get()
                        .uri(p.url())
                        .retrieve()
                        .body(GetPokemonDetailsResponse.class);
        pokemonsResult.add(
                new ExternalPokemonDto(
                        // TODO: Address NPE warning
                        pokemonDetails.name(),
                        pokemonDetails.weight(),
                        pokemonDetails.height(),
                        pokemonDetails.baseExperience()
                )
        );
    }
}
