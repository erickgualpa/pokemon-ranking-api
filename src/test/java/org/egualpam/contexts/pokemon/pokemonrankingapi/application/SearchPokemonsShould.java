package org.egualpam.contexts.pokemon.pokemonrankingapi.application;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.SearchPokemonsPort;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.InvalidSortingMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchPokemonsShould {

    @Mock
    private SearchPokemonsPort pokemonRepository;

    private SearchPokemons testSubject;

    @BeforeEach
    void setUp() {
        testSubject = new SearchPokemons(pokemonRepository);
    }

    @ValueSource(strings = {"weight", "height", "base_experience"})
    @ParameterizedTest
    void searchPokemons(String sortBy) {
        String pokemonName = randomAlphabetic(5);

        ArgumentCaptor<PokemonCriteria> criteriaCaptor = ArgumentCaptor.forClass(PokemonCriteria.class);
        when(pokemonRepository.find(criteriaCaptor.capture())).thenReturn(
                List.of(new PokemonDto(pokemonName))
        );

        SearchPokemonsQuery searchPokemonsQuery = new SearchPokemonsQuery(sortBy);
        List<PokemonDto> result = testSubject.execute(searchPokemonsQuery);

        assertThat(result).containsExactly(new PokemonDto(pokemonName));
        assertThat(criteriaCaptor.getValue()).usingRecursiveComparison().isEqualTo(new PokemonCriteria(sortBy));
    }

    @Test
    void throwDomainException_whenSortingMethodIsInvalid() {
        String invalidSortingMethod = randomAlphabetic(5);
        SearchPokemonsQuery searchPokemonsQuery = new SearchPokemonsQuery(invalidSortingMethod);
        assertThrows(InvalidSortingMethod.class, () -> testSubject.execute(searchPokemonsQuery));
    }
}