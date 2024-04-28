package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Limit;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.SortBy;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.RequiredPropertyIsMissing;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

public final class PokemonRepositoryFacade implements AggregateRepository<Pokemon> {

    private final WebClient webClient;
    private final String pokeApiHost;
    private final String getPokemonsPath;

    public PokemonRepositoryFacade(WebClient webClient, String pokeApiHost, String getPokemonsPath) {
        this.webClient = webClient;
        this.pokeApiHost = pokeApiHost;
        this.getPokemonsPath = getPokemonsPath;
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
        GetPokemonsResponse pokemons =
                webClient
                        .get()
                        .uri(pokeApiHost + getPokemonsPath)
                        .retrieve()
                        .bodyToMono(GetPokemonsResponse.class)
                        .block();

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

    record GetPokemonsResponse(List<GetPokemonsResponse.Pokemon> results) {
        record Pokemon(String name, String url) {
        }
    }

    record GetPokemonDetailsResponse(
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
