package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration",
                "org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.in.controllers"
        }
)
public class PokemonRankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokemonRankingApiApplication.class, args);
    }

}
