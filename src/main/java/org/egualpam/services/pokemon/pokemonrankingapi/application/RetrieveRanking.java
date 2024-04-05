package org.egualpam.services.pokemon.pokemonrankingapi.application;

import org.egualpam.services.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingId;
import org.egualpam.services.pokemon.pokemonrankingapi.domain.RankingRepository;

import java.util.List;

public class RetrieveRanking {

    private final RankingRepository rankingRepository;

    public RetrieveRanking(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public RankingDTO execute(RetrieveRankingQuery query) {
        RankingId rankingId = RankingId.valueOf(query.rankingId());
        List<Pokemon> pokemons = rankingRepository.find(rankingId).getPokemons();
        return new RankingDTO(
                pokemons.stream()
                        .limit(query.rankingLimit())
                        .map(pokemon -> new RankingDTO.Pokemon(pokemon.name()))
                        .toList()
        );
    }
}
