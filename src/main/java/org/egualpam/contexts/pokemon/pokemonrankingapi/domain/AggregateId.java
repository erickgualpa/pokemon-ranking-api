package org.egualpam.contexts.pokemon.pokemonrankingapi.domain;

import java.util.Objects;

public final class AggregateId {

    private final UniqueId value;

    public AggregateId(String value) {
        this.value = new UniqueId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregateId that = (AggregateId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
