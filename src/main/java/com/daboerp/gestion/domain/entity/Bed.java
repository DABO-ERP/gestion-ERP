package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.BedId;

import java.util.Objects;

/**
 * Bed entity - represents an individual bed within a room.
 */
public class Bed {
    
    private final BedId id;
    private final Integer bedNumber;
    private final Room room;
    
    private Bed(BedId id, Integer bedNumber, Room room) {
        this.id = Objects.requireNonNull(id, "Bed ID cannot be null");
        this.bedNumber = Objects.requireNonNull(bedNumber, "Bed number cannot be null");
        this.room = Objects.requireNonNull(room, "Room cannot be null");
        
        if (bedNumber <= 0) {
            throw new IllegalArgumentException("Bed number must be positive");
        }
    }
    
    public static Bed create(Integer bedNumber, Room room) {
        BedId id = BedId.generate();
        return new Bed(id, bedNumber, room);
    }
    
    public static Bed reconstitute(BedId id, Integer bedNumber, Room room) {
        return new Bed(id, bedNumber, room);
    }
    
    public BedId getId() {
        return id;
    }
    
    public Integer getBedNumber() {
        return bedNumber;
    }
    
    public Room getRoom() {
        return room;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bed bed = (Bed) o;
        return Objects.equals(id, bed.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
