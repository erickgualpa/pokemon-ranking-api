package org.egualpam.services.pokemon.pokemonrankingapi.e2e;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import org.egualpam.services.pokemon.pokemonrankingapi.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FindTop5HeaviestPokemonsFeature extends AbstractIntegrationTest {

    @Test
    void findTop5HeaviestPokemons() throws Exception {
        Body allPokemonsStubResponseBody = Body.fromJsonBytes(getAllPokemonsStubResponse().getBytes());

        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/api/v2/pokemon?limit=100000&offset=0"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withResponseBody(allPokemonsStubResponseBody)
                        )
        );

        stubPokemonDetailsResponse(1, 5);
        stubPokemonDetailsResponse(2, 100);
        stubPokemonDetailsResponse(3, 35);
        stubPokemonDetailsResponse(4, 40);
        stubPokemonDetailsResponse(5, 80);
        stubPokemonDetailsResponse(6, 15);

        mockMvc.perform(get("/api/v1/pokemon-ranking/heaviest"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                {
                                    "ranking": [
                                      {
                                        "name": "ivysaur"
                                      },
                                      {
                                        "name": "charmeleon"
                                      },
                                      {
                                        "name": "charmander"
                                      },
                                      {
                                        "name": "venusaur"
                                      },
                                      {
                                        "name": "charizard"
                                      }
                                    ]
                                  }
                                """
                        )
                );
    }

    private static void stubPokemonDetailsResponse(Integer pokemonId, Integer pokemonWeight) {
        Body singlePokemonStubResponseBody = Body.fromJsonBytes(getSinglePokemonStubResponse(pokemonWeight).getBytes());
        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/api/v2/pokemon/" + pokemonId + "/"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withResponseBody(singlePokemonStubResponseBody)
                        )
        );
    }

    private static String getAllPokemonsStubResponse() {
        return """
                {
                  "count": 1302,
                  "results": [
                    {
                      "name": "bulbasaur",
                      "url": "http://localhost:8081/api/v2/pokemon/1/"
                    },
                    {
                      "name": "ivysaur",
                      "url": "http://localhost:8081/api/v2/pokemon/2/"
                    },
                    {
                      "name": "venusaur",
                      "url": "http://localhost:8081/api/v2/pokemon/3/"
                    },
                    {
                      "name": "charmander",
                      "url": "http://localhost:8081/api/v2/pokemon/4/"
                    },
                    {
                      "name": "charmeleon",
                      "url": "http://localhost:8081/api/v2/pokemon/5/"
                    },
                    {
                      "name": "charizard",
                      "url": "http://localhost:8081/api/v2/pokemon/6/"
                    }
                  ]
                }
                """;
    }

    private static String getSinglePokemonStubResponse(Integer weight) {
        return """
                {
                  "weight": %d
                }
                """.formatted(weight);
    }
}
