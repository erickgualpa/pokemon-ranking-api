package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.FindPokemons;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.repositories.PokemonRepositoryFacade;
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
    public AggregateRepository<Pokemon> pokemonRepository(@Value("${clients.poke-api.host}") String host) {
        return new PokemonRepositoryFacade(host);
    }

    @Bean
    public FindPokemons findPokemon(AggregateRepository<Pokemon> pokemonRepository) {
        return new FindPokemons(pokemonRepository);
    }
}
