package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Limit;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.SortBy;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.RequiredPropertyIsMissing;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

public final class PokemonRepositoryFacade implements AggregateRepository<Pokemon> {

    // TODO: This is here just to do the trick with the URLs but it should be amended
    private final String pokeApiHost;
    private final WebClient pokeApiClient;

    public PokemonRepositoryFacade(String pokeApiHost) {
        this.pokeApiHost = pokeApiHost;
        // TODO: Current webClient configuration not is production ready, then it should be amended
        this.pokeApiClient = WebClient
                .builder()
                .baseUrl(pokeApiHost)
                .exchangeStrategies(
                        ExchangeStrategies
                                .builder()
                                .codecs(codecs -> codecs
                                        .defaultCodecs()
                                        .maxInMemorySize(10 * 1024 * 1024))
                                .build())
                .build();
    }

    @Override
    public List<Pokemon> findMatching(Criteria criteria) {
        PokemonCriteria pokemonCriteria = (PokemonCriteria) criteria;
        SortBy sortBy = pokemonCriteria.getType().orElseThrow(RequiredPropertyIsMissing::new);
        Limit limit = pokemonCriteria.getLimit().orElseThrow(RequiredPropertyIsMissing::new);
        return switch (sortBy) {
            case HEIGHT -> findSortedByHeight(limit);
            case WEIGHT -> findSortedByWeight(limit);
            case BASE_EXPERIENCE -> findSortedByBaseExperience(limit);
        };
    }

    private List<Pokemon> findSortedByHeight(Limit limit) {
        return getPokemonDetails().stream()
                .filter(p -> p.height() != null)
                .sorted((p1, p2) -> p2.height().compareTo(p1.height()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

    private List<Pokemon> findSortedByWeight(Limit limit) {
        return getPokemonDetails().stream()
                .filter(p -> p.weight() != null)
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

    private List<Pokemon> findSortedByBaseExperience(Limit limit) {
        return getPokemonDetails().stream()
                .filter(p -> p.baseExperience() != null)
                .sorted((p1, p2) -> p2.baseExperience().compareTo(p1.baseExperience()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

    private List<PokemonDTO> getPokemonDetails() {
        List<GetAllPokemonsResponse.Pokemon> allPokemons = getAllPokemons().results();
        List<GetSinglePokemonResponse> allPokemonDetails = getAllPokemonsDetails(allPokemons);
        return emptyIfNull(allPokemonDetails).stream()
                .map(r -> new PokemonDTO(r.name(), r.weight(), r.height(), r.baseExperience()))
                .toList();
    }

    private GetAllPokemonsResponse getAllPokemons() {
        GetAllPokemonsResponse getAllPokemonsResponse;
        try {
            getAllPokemonsResponse =
                    pokeApiClient
                            .get()
                            .uri("/api/v2/pokemon?limit=100000&offset=0")
                            .retrieve()
                            .bodyToMono(GetAllPokemonsResponse.class)
                            .block();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error retrieving all pokemons from pokeApi", e);
        }

        return Optional.ofNullable(getAllPokemonsResponse)
                .orElseThrow(() -> new RuntimeException("No pokemons found in the response"));
    }

    /*
     * Following workaround has been done considering the article:
     *  - https://www.baeldung.com/spring-webclient-simultaneous-calls#1-multiple-calls-to-the-same-service
     * Could be improved though
     * */
    private List<GetSinglePokemonResponse> getAllPokemonsDetails(List<GetAllPokemonsResponse.Pokemon> allPokemons) {
        return Flux.fromIterable(allPokemons)
                .flatMap(r -> getSinglePokemonDetailsMono(r.url()))
                .collectList()
                .block();
    }

    private Mono<GetSinglePokemonResponse> getSinglePokemonDetailsMono(String pokemonUrl) {
        try {
            return pokeApiClient
                    .get()
                    .uri(StringUtils.substringAfter(pokemonUrl, pokeApiHost))
                    .retrieve()
                    .bodyToMono(GetSinglePokemonResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error retrieving pokemon details from pokeApi", e);
        }
    }

    record GetAllPokemonsResponse(List<GetAllPokemonsResponse.Pokemon> results) {
        record Pokemon(String name, String url) {
        }
    }

    record GetSinglePokemonResponse(
            String name,
            Integer weight,
            Integer height,
            @JsonProperty("base_experience")
            Integer baseExperience
    ) {
    }

    record PokemonDTO(String name, Integer weight, Integer height, Integer baseExperience) {
    }
}
