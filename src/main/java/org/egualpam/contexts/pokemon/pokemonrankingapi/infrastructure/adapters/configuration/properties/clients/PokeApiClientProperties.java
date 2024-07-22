package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration.properties.clients;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients.poke-api")
public final class PokeApiClientProperties {
    private final String host;
    private final GetPokemons getPokemons;

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
