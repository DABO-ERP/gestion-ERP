package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Reservation identifier value object.
 */
public class ReservationId {
    
    private final String value;
    
    private ReservationId(String value) {
        Objects.requireNonNull(value, "Reservation ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Reservation ID cannot be blank");
        }
        this.value = value;
    }
    
    public static ReservationId of(String value) {
        return new ReservationId(value);
    }
    
    public static ReservationId generate() {
        return new ReservationId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return Objects.equals(value, that.value);
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
