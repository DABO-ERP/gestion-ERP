package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Bed identifier value object.
 */
public class BedId {
    
    private final String value;
    
    private BedId(String value) {
        Objects.requireNonNull(value, "Bed ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Bed ID cannot be blank");
        }
        this.value = value;
    }
    
    public static BedId of(String value) {
        return new BedId(value);
    }
    
    public static BedId generate() {
        return new BedId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BedId bedId = (BedId) o;
        return Objects.equals(value, bedId.value);
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
