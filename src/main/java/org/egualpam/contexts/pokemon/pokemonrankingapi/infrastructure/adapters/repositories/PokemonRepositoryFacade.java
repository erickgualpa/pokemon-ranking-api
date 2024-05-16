package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories;

import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.AggregateRepository;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Criteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Limit;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.Pokemon;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.PokemonCriteria;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.SortBy;
import org.egualpam.contexts.pokemon.pokemonrankingapi.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.repositories.suppliers.pokemons.PokemonDTO;

import java.util.List;
import java.util.function.Supplier;

public final class PokemonRepositoryFacade implements AggregateRepository<Pokemon> {

    private final Supplier<List<PokemonDTO>> pokemonsSupplier;

    public PokemonRepositoryFacade(Supplier<List<PokemonDTO>> pokemonsSupplier) {
        this.pokemonsSupplier = pokemonsSupplier;
    }

    @Override
    public List<Pokemon> findMatching(Criteria criteria) {
        PokemonCriteria pokemonCriteria = (PokemonCriteria) criteria;
        SortBy sortBy = pokemonCriteria.getType().orElseThrow(RequiredPropertyIsMissing::new);
        Limit limit = pokemonCriteria.getLimit().orElseThrow(RequiredPropertyIsMissing::new);
        return switch (sortBy) {
            case HEIGHT -> findSortedByHeight(limit);
            case WEIGHT -> findSortedByWeight(limit);
            case BASE_EXPERIENCE -> findSortedByBaseExperience(limit);
        };
    }

    private List<Pokemon> findSortedByHeight(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.height() != null)
                .sorted((p1, p2) -> p2.height().compareTo(p1.height()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

    private List<Pokemon> findSortedByWeight(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.weight() != null)
                .sorted((p1, p2) -> p2.weight().compareTo(p1.weight()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }

    private List<Pokemon> findSortedByBaseExperience(Limit limit) {
        return pokemonsSupplier.get().stream()
                .filter(p -> p.baseExperience() != null)
                .sorted((p1, p2) -> p2.baseExperience().compareTo(p1.baseExperience()))
                .limit(limit.value())
                .map(p -> new Pokemon(p.name()))
                .toList();
    }
}
