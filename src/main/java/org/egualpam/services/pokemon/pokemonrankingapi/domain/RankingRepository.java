package org.egualpam.services.pokemon.pokemonrankingapi.domain;

public interface RankingRepository {
    Ranking find(RankingId criteria);
}
