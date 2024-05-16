package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.concurrent;

import com.github.tomakehurst.wiremock.http.Body;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.function.Supplier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.http.Body.fromJsonBytes;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

class ConcurrentPokemonsSupplierIT extends AbstractIntegrationTest {

    private static final String CHARMELEON = "charmeleon";
    private static final String BULBASAUR = "bulbasaur";

    @Value("${clients.poke-api.host}")
    private String pokeApiHost;

    @Value("${clients.poke-api.get-pokemons.path}")
    private String getPokemonsPath;

    private Supplier<List<PokemonDTO>> pokemonsSupplier;

    private static void stubPokemonDetails(
            Integer id,
            String name,
            Integer weight,
            Integer height,
            Integer baseExperience
    ) {
        Body singlePokemonStubResponseBody = Body.fromJsonBytes("""
                    {
                      "name": "%s",
                      "weight": %d,
                      "height": %d,
                      "base_experience": %d
                    }
                """.formatted(name, weight, height, baseExperience).getBytes()
        );

        wireMockServer.stubFor(
                get(urlEqualTo("/api/v2/pokemon/" + id + "/"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withResponseBody(singlePokemonStubResponseBody)
                        )
        );
    }

    @BeforeEach
    void setUp() {
        pokemonsSupplier = new ConcurrentPokemonsSupplier(pokeApiHost, getPokemonsPath);
    }

    @Test
    void pokemonsGetSupplied() {
        Integer charmeleonId = nextInt(1, 10);
        Integer charmeleonWeight = nextInt(1, 100);
        Integer charmeleonHeight = nextInt(1, 100);
        Integer charmeleonBaseExperience = nextInt(1, 100);

        Integer bulbasaurId = nextInt(1, 10);
        Integer bulbasaurWeight = nextInt(1, 100);
        Integer bulbasaurHeight = nextInt(1, 100);
        Integer bulbasaurBaseExperience = nextInt(1, 100);

        Body allPokemonsStubResponseBody = fromJsonBytes("""
                    {
                      "count": 1,
                      "results": [
                        {
                          "name": "%s",
                          "url": "http://localhost:8081/api/v2/pokemon/%d/"
                        },
                        {
                          "name": "%s",
                          "url": "http://localhost:8081/api/v2/pokemon/%d/"
                        }
                      ]
                    }
                """
                .formatted(
                        CHARMELEON, charmeleonId,
                        BULBASAUR, bulbasaurId
                ).getBytes()
        );

        wireMockServer.stubFor(
                get(urlEqualTo("/api/v2/pokemon?limit=100000&offset=0"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withResponseBody(allPokemonsStubResponseBody)
                        )
        );

        stubPokemonDetails(charmeleonId, CHARMELEON, charmeleonWeight, charmeleonHeight, charmeleonBaseExperience);
        stubPokemonDetails(bulbasaurId, BULBASAUR, bulbasaurWeight, bulbasaurHeight, bulbasaurBaseExperience);

        List<PokemonDTO> result = pokemonsSupplier.get();

        assertThat(result).containsExactlyInAnyOrder(
                new PokemonDTO(CHARMELEON, charmeleonWeight, charmeleonHeight, charmeleonBaseExperience),
                new PokemonDTO(BULBASAUR, bulbasaurWeight, bulbasaurHeight, bulbasaurBaseExperience)
        );
    }
}