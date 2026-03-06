package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * RoomType identifier value object.
 */
public class RoomTypeId {
    
    private final String value;
    
    private RoomTypeId(String value) {
        Objects.requireNonNull(value, "Room type ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Room type ID cannot be blank");
        }
        this.value = value;
    }
    
    public static RoomTypeId of(String value) {
        return new RoomTypeId(value);
    }
    
    public static RoomTypeId generate() {
        return new RoomTypeId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomTypeId that = (RoomTypeId) o;
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
