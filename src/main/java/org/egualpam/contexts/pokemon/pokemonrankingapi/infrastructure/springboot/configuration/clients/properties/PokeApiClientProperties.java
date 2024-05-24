package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.springboot.configuration.clients.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients.poke-api")
public class PokeApiClientProperties {
    private String host;
    private GetPokemons getPokemons;

    public PokeApiClientProperties(String host, GetPokemons getPokemons) {
        this.host = host;
        this.getPokemons = getPokemons;
    }

    public String host() {
        return host;
    }

    public String getPokemonsPath() {
        return getPokemons.path();
    }
}
