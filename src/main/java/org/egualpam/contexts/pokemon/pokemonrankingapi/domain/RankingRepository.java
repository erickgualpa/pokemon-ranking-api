package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

public interface RankingRepository {
    Ranking find(RankingId rankingId);
}
