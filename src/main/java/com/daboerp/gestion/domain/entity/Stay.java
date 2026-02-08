package com.daboerp.gestion.domain.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Stay entity - represents the actual stay period derived from a reservation.
 */
public class Stay {
    
    private final String id;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    
    private Stay(String id, LocalDate checkIn, LocalDate checkOut) {
        this.id = Objects.requireNonNull(id, "Stay ID cannot be null");
        this.checkIn = Objects.requireNonNull(checkIn, "Check-in date cannot be null");
        this.checkOut = checkOut; // Can be null until checkout
    }
    
    public static Stay create(LocalDate checkIn, LocalDate checkOut) {
        return new Stay(UUID.randomUUID().toString(), checkIn, checkOut);
    }
    
    public static Stay reconstitute(String id, LocalDate checkIn, LocalDate checkOut) {
        return new Stay(id, checkIn, checkOut);
    }
    
    public boolean isActive() {
        return checkOut == null;
    }
    
    public long getNightCount() {
        if (checkOut == null) {
            return java.time.temporal.ChronoUnit.DAYS.between(checkIn, LocalDate.now());
        }
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }
    
    public String getId() {
        return id;
    }
    
    public LocalDate getCheckIn() {
        return checkIn;
    }
    
    public LocalDate getCheckOut() {
        return checkOut;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stay stay = (Stay) o;
        return Objects.equals(id, stay.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
