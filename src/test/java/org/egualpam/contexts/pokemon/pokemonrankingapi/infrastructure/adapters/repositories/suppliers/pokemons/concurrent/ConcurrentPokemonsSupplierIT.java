package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.concurrent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.http.Body;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration.properties.clients.PokeApiClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Supplier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.common.Strings.randomAlphabetic;
import static com.github.tomakehurst.wiremock.http.Body.fromJsonBytes;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

class ConcurrentPokemonsSupplierIT extends AbstractIntegrationTest {

    @Autowired
    PokeApiClientProperties pokeApiClientProperties;
    @Autowired
    private ObjectMapper objectMapper;
    private Supplier<List<PokemonDTO>> pokemonsSupplier;

    private static void stubPokemonDetails(
            Integer id,
            String name,
            Integer weight,
            Integer height,
            Integer baseExperience
    ) {
        Body singlePokemonStubResponseBody = fromJsonBytes("""
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
        pokemonsSupplier = new ConcurrentPokemonsSupplier(pokeApiClientProperties);
    }

    @Test
    void pokemonsGetSupplied() throws JsonProcessingException {
        Integer pokemonsAmount = 1500;

        ObjectNode allPokemonsStubResponseBody = objectMapper.createObjectNode();
        allPokemonsStubResponseBody.put("count", pokemonsAmount);
        ArrayNode results = allPokemonsStubResponseBody.putArray("results");

        for (int i = 0; i < pokemonsAmount; i++) {
            String pokemonName = randomAlphabetic(5);
            Integer pokemonId = i;
            results.addObject()
                    .put("name", pokemonName)
                    .put("url", "http://localhost:8081/api/v2/pokemon/" + pokemonId + "/");

            Integer pokemonWeight = nextInt(1, 100);
            Integer pokemonHeight = nextInt(1, 100);
            Integer pokemonBaseExperience = nextInt(1, 100);
            stubPokemonDetails(pokemonId, pokemonName, pokemonWeight, pokemonHeight, pokemonBaseExperience);
        }

        wireMockServer.stubFor(
                get(urlEqualTo("/api/v2/pokemon?limit=100000&offset=0"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withResponseBody(
                                                fromJsonBytes(
                                                        objectMapper.writeValueAsBytes(allPokemonsStubResponseBody)
                                                )
                                        )
                        )
        );

        List<PokemonDTO> result = pokemonsSupplier.get();

        assertThat(result).hasSize(pokemonsAmount);
    }
}