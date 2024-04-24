package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPokemonShould {

    private FindPokemon testee;

    @Captor
    private ArgumentCaptor<PokemonCriteria> criteriaCaptor;

    @Mock
    private AggregateRepository<PokemonEntity> pokemonRepository;

    @BeforeEach
    void setUp() {
        testee = new FindPokemon(pokemonRepository);
    }

    @Test
    void findPokemon() {
        String pokemonName = randomAlphabetic(5);
        when(pokemonRepository.findMatching(criteriaCaptor.capture())).thenReturn(
                List.of(new PokemonEntity(pokemonName))
        );

        FindPokemonQuery weight = new FindPokemonQuery("weight");
        List<PokemonDTO> result = testee.execute(weight);

        assertThat(result).containsExactly(new PokemonDTO(pokemonName));
        assertThat(criteriaCaptor.getValue()).usingRecursiveComparison().isEqualTo(new PokemonCriteria("weight"));
    }
}