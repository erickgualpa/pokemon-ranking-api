package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.webflux;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.PokemonSearchRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Limit;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.SortBy;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration.properties.clients.PokeApiClientProperties;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared.ExternalPokemonDto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared.ExternalPokemonDto.*;

public final class PokemonSearchRepositoryWebFluxAdapter implements PokemonSearchRepository {

    private final Supplier<List<ExternalPokemonDto>> pokemonsSupplier;

    public PokemonSearchRepositoryWebFluxAdapter(PokeApiClientProperties pokeApiClientProperties) {
        this.pokemonsSupplier = new WebfluxPokemonsSupplier(pokeApiClientProperties);
    }

    @Override
    public List<PokemonDto> find(PokemonCriteria pokemonCriteria) {
        SortBy sortBy = pokemonCriteria.getType().orElseThrow(RequiredPropertyIsMissing::new);
        Limit limit = pokemonCriteria.getLimit().orElseThrow(RequiredPropertyIsMissing::new);

        List<ExternalPokemonDto> externalPokemons = switch (sortBy) {
            case HEIGHT -> sortedPokemons(hasHeightInformed, heightComparator);
            case WEIGHT -> sortedPokemons(hasWeightInformed, weightComparator);
            case BASE_EXPERIENCE -> sortedPokemons(hasBaseExperienceInformed, baseExperienceComparator);
        };

        return externalPokemons.stream()
                .limit(limit.value())
                .map(p -> new PokemonDto(p.name()))
                .toList();
    }

    private List<ExternalPokemonDto> sortedPokemons(
            Predicate<ExternalPokemonDto> pokemonValidation,
            Comparator<ExternalPokemonDto> pokemonComparator
    ) {
        return pokemonsSupplier.get().stream()
                .filter(pokemonValidation)
                .sorted(pokemonComparator)
                .toList();
    }
}
