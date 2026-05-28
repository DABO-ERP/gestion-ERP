package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class AmenityDefinitionId {

    private final String value;

    private AmenityDefinitionId(String value) {
        Objects.requireNonNull(value, "AmenityDefinition ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("AmenityDefinition ID cannot be blank");
        }
        this.value = value;
    }

    public static AmenityDefinitionId of(String value) {
        return new AmenityDefinitionId(value);
    }

    public static AmenityDefinitionId generate() {
        return new AmenityDefinitionId(UUID.randomUUID().toString());
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmenityDefinitionId that = (AmenityDefinitionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
