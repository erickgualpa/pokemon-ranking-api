package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.RetrieveRanking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.repositories.HttpRankingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonRankingApiConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Pokemon Ranking API")
                );
    }

    @Bean
    public AggregateRepository<Ranking> rankingRepository(@Value("${clients.poke-api.host}") String host) {
        return new HttpRankingRepository(host);
    }

    @Bean
    public RetrieveRanking retrieveRanking(AggregateRepository<Ranking> rankingRepository) {
        return new RetrieveRanking(rankingRepository);
    }
}
