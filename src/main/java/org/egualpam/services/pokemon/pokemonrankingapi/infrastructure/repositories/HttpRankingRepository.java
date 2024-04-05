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

    // TODO: This is here just to do the trick with the URLs but this should be amended
    private final String pokeApiHost;
    private final WebClient pokeApiClient;

    public HttpRankingRepository(String pokeApiHost) {
        this.pokeApiHost = pokeApiHost;
        this.pokeApiClient = WebClient
                .builder()
                .baseUrl(pokeApiHost)
                .exchangeStrategies(
                        ExchangeStrategies
                                .builder()
                                .codecs(codecs -> codecs
                                        .defaultCodecs()
                                        .maxInMemorySize(500 * 1024))
                                .build())
                .build();
    }

    @Override
    public Ranking find(RankingId rankingId) {
        Optional<GetAllPokemonsResponse> getAllPokemonsResponse = getAllPokemons();

        List<GetAllPokemonsResponse.Pokemon> allPokemonsFromResponse =
                getAllPokemonsResponse
                        .map(GetAllPokemonsResponse::results)
                        .orElseThrow(() -> new RuntimeException("No pokemons found"));

        List<PokemonDto> pokemons = allPokemonsFromResponse.stream()
                .map(
                        r -> {
                            Optional<GetSinglePokemonResponse> getSinglePokemonResponse =
                                    getSinglePokemonDetails(r.url());

                            Integer pokemonHeight = getSinglePokemonResponse.map(GetSinglePokemonResponse::weight)
                                    .orElseThrow(
                                            () -> new RuntimeException("No pokemon details found")
                                    );

                            return new PokemonDto(r.name, pokemonHeight);
                        })
                .toList();

        Ranking ranking = new Ranking(rankingId);

        pokemons.stream()
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .forEach(p -> ranking.addPokemon(p.name()));

        return ranking;
    }

    private Optional<GetAllPokemonsResponse> getAllPokemons() {
        GetAllPokemonsResponse getAllPokemonsResponse;
        try {
            getAllPokemonsResponse =
                    pokeApiClient
                            .get()
                            // TODO: Check the URL used here to avoid limits
                            .uri("/api/v2/pokemon/")
                            .retrieve()
                            .bodyToMono(GetAllPokemonsResponse.class)
                            .block();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error retrieving all pokemons from pokeApi", e);
        }
        return Optional.ofNullable(getAllPokemonsResponse);
    }

    private Optional<GetSinglePokemonResponse> getSinglePokemonDetails(String pokemonUrl) {
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
            throw new RuntimeException("Unexpected error retrieving single pokemon details from pokeApi", e);
        }
        return Optional.ofNullable(getSinglePokemonResponse);
    }

    record GetAllPokemonsResponse(List<Pokemon> results) {
        record Pokemon(String name, String url) {
        }
    }

    record GetSinglePokemonResponse(String name, Integer weight) {
    }

    record PokemonDto(String name, Integer weight) {
    }
}
