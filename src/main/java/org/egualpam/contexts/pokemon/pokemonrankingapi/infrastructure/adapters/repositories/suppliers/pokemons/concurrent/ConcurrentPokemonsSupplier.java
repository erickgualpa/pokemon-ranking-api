package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.concurrent;

import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonsResponse;
import org.slf4j.Logger;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.time.Instant.now;
import static org.slf4j.LoggerFactory.getLogger;

public final class ConcurrentPokemonsSupplier implements Supplier<List<PokemonDTO>> {

    private static final int THREAD_POOL_MAX_SIZE = 50;
    private final Logger logger = getLogger(ConcurrentPokemonsSupplier.class);

    private final RestClient restClient;
    private final String pokeApiHost;
    private final String getPokemonsPath;

    public ConcurrentPokemonsSupplier(String pokeApiHost, String getPokemonsPath) {
        this.restClient = RestClient.create();
        this.pokeApiHost = pokeApiHost;
        this.getPokemonsPath = getPokemonsPath;
    }

    @Override
    public List<PokemonDTO> get() {
        List<GetPokemonsResponse.Pokemon> pokemons = restClient.get()
                .uri(pokeApiHost + getPokemonsPath)
                .retrieve()
                .body(GetPokemonsResponse.class)
                // TODO: Address NPE warning
                .results();

        List<PokemonDTO> pokemonsResult = new ArrayList<>();

        Instant start = now();
        pokemons.forEach(p -> {
            Thread thread = new Thread(() -> {
                // TODO: Check if this whole method needs to be handled concurrently
                getPokemonDetails(p, pokemonsResult);
            });
            thread.start();
            if (Thread.activeCount() > THREAD_POOL_MAX_SIZE) {
                logger.info("Waiting for threads to finish");
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    logger.error("Error while joining thread", e);
                }
            }
        });
        logger.info("Pokemons retrieved in: {}", Duration.between(start, now()).toMillis());

        return pokemonsResult;
    }

    private void getPokemonDetails(GetPokemonsResponse.Pokemon p, List<PokemonDTO> pokemonsResult) {
        logger.info("Getting details for pokemon: {}", p.name());
        GetPokemonDetailsResponse pokemonDetails = restClient.get()
                .uri(p.url())
                .retrieve()
                .body(GetPokemonDetailsResponse.class);
        logger.info("Adding pokemon: {}", p.name());
        pokemonsResult.add(
                new PokemonDTO(
                        // TODO: Address NPE warning
                        pokemonDetails.name(),
                        pokemonDetails.weight(),
                        pokemonDetails.height(),
                        pokemonDetails.baseExperience()
                )
        );
    }
}
