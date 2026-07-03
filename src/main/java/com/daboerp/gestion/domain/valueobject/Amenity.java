package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;

/**
 * Immutable value object representing an amenity name or description.
 * Self-validating to ensure non-blank values.
 */
public class Amenity {

    private final String value;

    private Amenity(String value) {
        Objects.requireNonNull(value, "Amenity value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Amenity value cannot be blank");
        }
        this.value = value;
    }

    public static Amenity of(String value) {
        return new Amenity(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amenity amenity = (Amenity) o;
        return Objects.equals(value, amenity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
