package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingCriteria;

public class RetrieveRanking {

    private final AggregateRepository<Ranking> rankingRepository;

    public RetrieveRanking(AggregateRepository<Ranking> rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public RankingDTO execute(RetrieveRankingQuery query) {
        Criteria rankingCriteria = new RankingCriteria(query.id(), query.limit());
        Ranking ranking = rankingRepository.find(rankingCriteria);
        return new RankingDTO(
                ranking.getPokemons().stream()
                        .map(pokemon -> new RankingDTO.Pokemon(pokemon.name()))
                        .toList()
        );
    }
}
