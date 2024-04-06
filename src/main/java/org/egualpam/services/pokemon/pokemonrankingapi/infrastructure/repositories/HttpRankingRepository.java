package org.egualpam.services.pokemon.pokemonrankingapi.infrastructure.repositories;

import org.apache.commons.lang3.StringUtils;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingId;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingRepository;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

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
        throw new RuntimeException("Unexpected rankingId");
    }

    private Ranking findHeaviestPokemonsRanking(RankingId rankingId) {
        List<GetAllPokemonsResponse.Pokemon> allPokemonsFromResponse = getAllPokemons().results();

        List<PokemonDto> pokemons = allPokemonsFromResponse.stream()
                .map(
                        r -> {
                            GetSinglePokemonResponse pokemonDetails = getSinglePokemonDetails(r.url());
                            return new PokemonDto(r.name, pokemonDetails.weight(), pokemonDetails.height());
                        })
                .toList();

        Ranking ranking = new Ranking(rankingId);

        pokemons.stream()
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private Ranking findHighestPokemonRanking(RankingId rankingId) {
        List<GetAllPokemonsResponse.Pokemon> allPokemonsFromResponse = getAllPokemons().results();

        List<PokemonDto> pokemons = allPokemonsFromResponse.stream()
                .map(
                        r -> {
                            GetSinglePokemonResponse pokemonDetails = getSinglePokemonDetails(r.url());
                            return new PokemonDto(r.name, pokemonDetails.weight(), pokemonDetails.height());
                        })
                .toList();

        Ranking ranking = new Ranking(rankingId);

        pokemons.stream()
                .sorted((p1, p2) -> p2.height().compareTo(p1.height()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private GetAllPokemonsResponse getAllPokemons() {
        GetAllPokemonsResponse getAllPokemonsResponse;
        try {
            getAllPokemonsResponse =
                    pokeApiClient
                            .get()
                            // TODO: Check the URL used here to avoid limits
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

    private GetSinglePokemonResponse getSinglePokemonDetails(String pokemonUrl) {
        GetSinglePokemonResponse getSinglePokemonResponse;
        try {
            getSinglePokemonResponse =
                    pokeApiClient
                            .get()
                            .uri(StringUtils.substringAfter(pokemonUrl, pokeApiHost))
                            .retrieve()
                            .bodyToMono(GetSinglePokemonResponse.class)
                            .block();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error retrieving pokemon details from pokeApi", e);
        }

        return Optional.ofNullable(getSinglePokemonResponse)
                .orElseThrow(() -> new RuntimeException("No pokemon details found in the response"));
    }

    record GetAllPokemonsResponse(List<Pokemon> results) {
        record Pokemon(String name, String url) {
        }
    }

    record GetSinglePokemonResponse(String name, Integer weight, Integer height) {
    }

    record PokemonDto(String name, Integer weight, Integer height) {
    }
}
