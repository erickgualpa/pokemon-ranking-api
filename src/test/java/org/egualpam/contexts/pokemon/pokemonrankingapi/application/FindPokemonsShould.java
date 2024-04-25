package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.InvalidSortingMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPokemonsShould {

    private FindPokemons testee;

    @Captor
    private ArgumentCaptor<PokemonCriteria> criteriaCaptor;

    @Mock
    private AggregateRepository<Pokemon> pokemonRepository;

    @BeforeEach
    void setUp() {
        testee = new FindPokemons(pokemonRepository);
    }

    @ValueSource(strings = {"weight", "height", "base_experience"})
    @ParameterizedTest
    void findPokemon(String sortBy) {
        String pokemonName = randomAlphabetic(5);
        when(pokemonRepository.findMatching(criteriaCaptor.capture())).thenReturn(
                List.of(new Pokemon(pokemonName))
        );

        FindPokemonQuery findPokemonQuery = new FindPokemonQuery(sortBy);
        List<PokemonDTO> result = testee.execute(findPokemonQuery);

        assertThat(result).containsExactly(new PokemonDTO(pokemonName));
        assertThat(criteriaCaptor.getValue()).usingRecursiveComparison().isEqualTo(new PokemonCriteria(sortBy));
    }

    @Test
    void throwDomainException_whenSortingMethodIsInvalid() {
        String invalidSortingMethod = randomAlphabetic(5);
        FindPokemonQuery findPokemonQuery = new FindPokemonQuery(invalidSortingMethod);
        assertThrows(InvalidSortingMethod.class, () -> testee.execute(findPokemonQuery));
    }
}