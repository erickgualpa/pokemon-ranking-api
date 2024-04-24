package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Ranking;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.RankingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveRankingShould {

    @Mock
    private AggregateRepository<Ranking> rankingRepository;

    @Captor
    private ArgumentCaptor<Criteria> criteriaCaptor;

    private RetrieveRanking testee;

    @BeforeEach
    void setUp() {
        testee = new RetrieveRanking(rankingRepository);
    }

    @EnumSource(RankingType.class)
    @ParameterizedTest
    void retrieveRanking(RankingType rankingType) {
        Integer rankingLimit = 2;

        Ranking ranking = new Ranking(rankingType);
        ranking.addPokemon(randomAlphabetic(5));

        when(rankingRepository.find(criteriaCaptor.capture())).thenReturn(ranking);

        RankingDTO result = testee.execute(
                new RetrieveRankingQuery(rankingType.name(), rankingLimit)
        );

        Criteria criteria = criteriaCaptor.getValue();
        assertNotNull(criteria);
        assertThat(criteria).isInstanceOf(RankingCriteria.class);
        RankingCriteria rankingCriteria = (RankingCriteria) criteria;
        assertThat(rankingCriteria.getType()).contains(rankingType);
        assertThat(rankingCriteria.getLimit())
                .satisfies(actual -> {
                            assertThat(actual).isPresent();
                            assertThat(actual.get().value()).isEqualTo(rankingLimit);
                        }
                );

        assertNotNull(result);
        assertThat(result.pokemons()).containsExactly(
                new PokemonDTO(ranking.getPokemons().getFirst().name())
        );
    }
}
