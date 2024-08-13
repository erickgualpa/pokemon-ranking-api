package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.searchpokemons.webflux;

import org.egualpam.contexts.pokemon.pokemonrankingapi.application.PokemonDto;
import org.egualpam.contexts.pokemon.pokemonrankingapi.application.ports.out.PokemonSearchRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Limit;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.SortBy;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.configuration.properties.clients.PokeApiClientProperties;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.searchpokemons.shared.ExternalPokemonDto;

import java.util.List;
import java.util.function.Supplier;

public final class PokemonSearchRepositoryWebFluxAdapter implements PokemonSearchRepository {

    private final Supplier<List<ExternalPokemonDto>> pokemonsSupplier;

    public PokemonSearchRepositoryWebFluxAdapter(PokeApiClientProperties pokeApiClientProperties) {
        this.pokemonsSupplier = new WebfluxPokemonsSupplier(pokeApiClientProperties);
    }

    @Override
    public List<PokemonDto> find(PokemonCriteria pokemonCriteria) {
        SortBy sortBy = pokemonCriteria.getType().orElseThrow(RequiredPropertyIsMissing::new);
        Limit limit = pokemonCriteria.getLimit().orElseThrow(RequiredPropertyIsMissing::new);
        return switch (sortBy) {
            case HEIGHT -> findSortedByHeight(limit);
            case WEIGHT -> findSortedByWeight(limit);
            case BASE_EXPERIENCE -> findSortedByBaseExperience(limit);
        };
    }

    private List<PokemonDto> findSortedByHeight(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.height() != null)
                .sorted((p1, p2) -> p2.height().compareTo(p1.height()))
                .limit(limit.value())
                .map(p -> new PokemonDto(p.name()))
                .toList();
    }

    private List<PokemonDto> findSortedByWeight(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.weight() != null)
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .limit(limit.value())
                .map(p -> new PokemonDto(p.name()))
                .toList();
    }

    private List<PokemonDto> findSortedByBaseExperience(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.baseExperience() != null)
                .sorted((p1, p2) -> p2.baseExperience().compareTo(p1.baseExperience()))
                .limit(limit.value())
                .map(p -> new PokemonDto(p.name()))
                .toList();
    }
}
