package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.concurrent;

import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonDetailsResponse;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.shared.GetPokemonsResponse;
import org.slf4j.Logger;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

public final class ConcurrentPokemonsSupplier implements Supplier<List<PokemonDTO>> {

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

        List<Thread> threads = pokemons.stream()
                // TODO: Create threads smarter
                .map(
                        p -> new Thread(
                                () -> {
                                    logger.info("Getting details for pokemon: {}", p.name());
                                    GetPokemonDetailsResponse pokemonDetails = restClient.get()
                                            .uri(p.url())
                                            .retrieve()
                                            .body(GetPokemonDetailsResponse.class);
                                    synchronized (pokemonsResult) {
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
                        ))
                .toList();

        threads.forEach(Thread::start);

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
            }
        });

        return pokemonsResult;
    }
}
