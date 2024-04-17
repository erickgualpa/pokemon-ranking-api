package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingId;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingId.HEAVIEST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveRankingShould {

    @Mock
    private RankingRepository rankingRepository;
    private RetrieveRanking testee;

    @BeforeEach
    void setUp() {
        testee = new RetrieveRanking(rankingRepository);
    }

    @EnumSource(RankingId.class)
    @ParameterizedTest
    void returnHeaviestPokemonRanking(RankingId rankingId) {
        Ranking ranking = new Ranking(rankingId);
        ranking.getPokemons().addAll(
                List.of(
                        new Pokemon(randomAlphabetic(5)),
                        new Pokemon(randomAlphabetic(5)),
                        new Pokemon(randomAlphabetic(5)),
                        new Pokemon(randomAlphabetic(5)),
                        new Pokemon(randomAlphabetic(5))
                )
        );

        when(rankingRepository.find(HEAVIEST)).thenReturn(ranking);

        RankingDTO result = testee.execute(
                new RetrieveRankingQuery("HEAVIEST", 2)
        );

        assertNotNull(result);
        assertThat(result.pokemons())
                .hasSize(2)
                .containsExactly(
                        new RankingDTO.Pokemon(ranking.getPokemons().get(0).name()),
                        new RankingDTO.Pokemon(ranking.getPokemons().get(1).name())
                );
    }
}