package org.egualpam.contexts.pokemon.pokemonrankingapi.infrastructure.adapters.out.pokemonsearchrepository.shared;

import java.util.Comparator;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public record ExternalPokemonDto(String name, Integer weight, Integer height, Integer baseExperience) {
    public static final Predicate<ExternalPokemonDto> hasHeightInformed = p -> nonNull(p.height);
    public static final Predicate<ExternalPokemonDto> hasWeightInformed = p -> nonNull(p.weight);
    public static final Predicate<ExternalPokemonDto> hasBaseExperienceInformed = p -> nonNull(p.baseExperience);

    public static final Comparator<ExternalPokemonDto> heightComparator =
            (p1, p2) -> p2.height().compareTo(p1.height());
    public static final Comparator<ExternalPokemonDto> weightComparator =
            (p1, p2) -> p2.weight().compareTo(p1.weight());
    public static final Comparator<ExternalPokemonDto> baseExperienceComparator =
            (p1, p2) -> p2.baseExperience().compareTo(p1.baseExperience());
}
