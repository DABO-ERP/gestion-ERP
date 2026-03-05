package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Room aggregate root.
 * Represents a physical room in the hostel.
 * No framework dependencies - pure domain model.
 */
public class Room {
    
    private final RoomId id;
    private Integer roomNumber;
    private RoomType roomType;
    private RoomStatus roomStatus;
    private final List<Amenity> amenities;
    private final List<Bed> beds;
    private final java.time.LocalDate createdAt;
    
    private Room(RoomId id, Integer roomNumber, RoomType roomType, 
                 RoomStatus roomStatus, List<Amenity> amenities, java.time.LocalDate createdAt) {
        this.id = Objects.requireNonNull(id, "Room ID cannot be null");
        this.roomNumber = Objects.requireNonNull(roomNumber, "Room number cannot be null");
        this.roomType = Objects.requireNonNull(roomType, "Room type cannot be null");
        this.roomStatus = Objects.requireNonNull(roomStatus, "Room status cannot be null");
        this.amenities = new ArrayList<>(amenities != null ? amenities : Collections.emptyList());
        this.beds = new ArrayList<>();
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        
        validateRoomNumber(roomNumber);
    }
    
    public static Room create(Integer roomNumber, RoomType roomType, List<Amenity> amenities) {
        RoomId id = RoomId.generate();
        return new Room(id, roomNumber, roomType, RoomStatus.AVAILABLE, amenities, java.time.LocalDate.now());
    }
    
    public static Room reconstitute(RoomId id, Integer roomNumber, RoomType roomType,
                                   RoomStatus roomStatus, List<Amenity> amenities, 
                                   List<Bed> beds, java.time.LocalDate createdAt) {
        Room room = new Room(id, roomNumber, roomType, roomStatus, amenities, createdAt);
        if (beds != null) {
            for (Bed bed : beds) {
                bed.setRoom(room);
            }
            room.beds.addAll(beds);
        }
        return room;
    }
    
    // Business methods
    public void addBed(Integer bedNumber) {
        if (beds.stream().anyMatch(b -> b.getBedNumber().equals(bedNumber))) {
            throw new IllegalArgumentException("Bed number " + bedNumber + " already exists in room");
        }
        Bed bed = Bed.create(bedNumber, this);
        beds.add(bed);
    }
    
    public void removeBed(Integer bedNumber) {
        beds.removeIf(b -> b.getBedNumber().equals(bedNumber));
    }
    
    public void markAsAvailable() {
        this.roomStatus = RoomStatus.AVAILABLE;
    }
    
    public void markAsOccupied() {
        if (this.roomStatus == RoomStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException("Cannot occupy room that is out of service");
        }
        this.roomStatus = RoomStatus.OCCUPIED;
    }
    
    public void markAsOutOfService() {
        this.roomStatus = RoomStatus.OUT_OF_SERVICE;
    }
    
    public void updateRoomType(RoomType roomType) {
        this.roomType = Objects.requireNonNull(roomType, "Room type cannot be null");
    }
    
    public void addAmenity(Amenity amenity) {
        if (!amenities.contains(amenity)) {
            amenities.add(amenity);
        }
    }
    
    public void removeAmenity(Amenity amenity) {
        amenities.remove(amenity);
    }
    
    public boolean isAvailable() {
        return roomStatus == RoomStatus.AVAILABLE;
    }
    
    public boolean canAccommodate(int numberOfGuests) {
        return roomType.getMaxOccupancy() >= numberOfGuests;
    }
    
    private void validateRoomNumber(Integer roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
    }
    
    // Getters
    public RoomId getId() {
        return id;
    }
    
    public Integer getRoomNumber() {
        return roomNumber;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }
    
    public List<Amenity> getAmenities() {
        return Collections.unmodifiableList(amenities);
    }
    
    public List<Bed> getBeds() {
        return Collections.unmodifiableList(beds);
    }
    
    public java.time.LocalDate getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
