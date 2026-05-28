package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class RoomBlockId {

    private final String value;

    private RoomBlockId(String value) {
        Objects.requireNonNull(value, "RoomBlock ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("RoomBlock ID cannot be blank");
        }
        this.value = value;
    }

    public static RoomBlockId of(String value) {
        return new RoomBlockId(value);
    }

    public static RoomBlockId generate() {
        return new RoomBlockId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomBlockId that = (RoomBlockId) o;
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
