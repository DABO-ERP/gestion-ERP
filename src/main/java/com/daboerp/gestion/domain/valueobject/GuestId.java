package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Guest identifier value object.
 * Immutable and self-validating.
 */
public class GuestId {
    
    private final String value;
    
    private GuestId(String value) {
        Objects.requireNonNull(value, "Guest ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Guest ID cannot be blank");
        }
        this.value = value;
    }
    
    public static GuestId of(String value) {
        return new GuestId(value);
    }
    
    public static GuestId generate() {
        return new GuestId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuestId guestId = (GuestId) o;
        return Objects.equals(value, guestId.value);
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
