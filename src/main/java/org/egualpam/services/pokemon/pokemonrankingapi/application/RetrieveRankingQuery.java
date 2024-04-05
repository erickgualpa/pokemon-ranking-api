package org.egualpam.services.pokemon.pokemonrankingapi.application;

public record RetrieveRankingQuery(
        String rankingId,
        Integer rankingLimit
) {
}
