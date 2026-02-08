package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Room identifier value object.
 */
public class RoomId {
    
    private final String value;
    
    private RoomId(String value) {
        Objects.requireNonNull(value, "Room ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Room ID cannot be blank");
        }
        this.value = value;
    }
    
    public static RoomId of(String value) {
        return new RoomId(value);
    }
    
    public static RoomId generate() {
        return new RoomId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomId roomId = (RoomId) o;
        return Objects.equals(value, roomId.value);
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
