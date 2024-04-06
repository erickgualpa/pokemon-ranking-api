package org.egualpam.services.pokemon.pokemonrankingapi.infrastructure.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingId;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingRepository;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

public class HttpRankingRepository implements RankingRepository {

    // TODO: This is here just to do the trick with the URLs but it should be amended
    private final String pokeApiHost;
    private final WebClient pokeApiClient;

    public HttpRankingRepository(String pokeApiHost) {
        this.pokeApiHost = pokeApiHost;
        // TODO: Current webClient configuration is production ready, then it should be amended
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
    public Ranking find(RankingId rankingId) {
        if (RankingId.HEAVIEST == rankingId) {
            return findHeaviestPokemonsRanking(rankingId);
        }
        if (RankingId.HIGHEST == rankingId) {
            return findHighestPokemonRanking(rankingId);
        }
        if (RankingId.MOST_EXPERIENCED == rankingId) {
            return findMostExperiencedPokemonsRanking(rankingId);
        }
        throw new RuntimeException("Unexpected rankingId");
    }

    private Ranking findHeaviestPokemonsRanking(RankingId rankingId) {
        Ranking ranking = new Ranking(rankingId);

        getPokemonDetails().stream()
                .filter(p -> p.weight() != null)
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private Ranking findHighestPokemonRanking(RankingId rankingId) {
        Ranking ranking = new Ranking(rankingId);

        getPokemonDetails().stream()
                .filter(p -> p.height() != null)
                .sorted((p1, p2) -> p2.height().compareTo(p1.height()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private Ranking findMostExperiencedPokemonsRanking(RankingId rankingId) {
        Ranking ranking = new Ranking(rankingId);

        getPokemonDetails().stream()
                .filter(p -> p.baseExperience() != null)
                .sorted((p1, p2) -> p2.baseExperience().compareTo(p1.baseExperience()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private List<PokemonDto> getPokemonDetails() {
        List<GetAllPokemonsResponse.Pokemon> allPokemons = getAllPokemons().results();
        List<GetSinglePokemonResponse> allPokemonDetails = getAllPokemonsDetails(allPokemons);
        return emptyIfNull(allPokemonDetails).stream()
                .map(r -> new PokemonDto(r.name(), r.weight(), r.height(), r.baseExperience()))
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

    record GetAllPokemonsResponse(List<Pokemon> results) {
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

    record PokemonDto(String name, Integer weight, Integer height, Integer baseExperience) {
    }
}
