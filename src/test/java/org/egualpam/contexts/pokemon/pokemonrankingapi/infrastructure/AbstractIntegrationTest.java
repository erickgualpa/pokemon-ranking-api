package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.configuration.PokemonRankingApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@SpringBootTest(
        classes = PokemonRankingApiApplication.class,
        webEnvironment = RANDOM_PORT
)
@ContextConfiguration(
        classes = {PokemonRankingApiConfiguration.class}
)
public abstract class AbstractIntegrationTest {

    protected static final WireMockServer wireMockServer = new WireMockServer(8081);

    static {
        wireMockServer.start();
    }

    @Autowired
    protected MockMvc mockMvc;
}
