package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Room aggregate root.
 * Represents a physical room in the hostel.
 * No framework dependencies - pure domain model.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {
    
    @EqualsAndHashCode.Include
    private final RoomId id;
    @Setter
    private Integer roomNumber;
    @Setter
    private RoomType roomType;
    @Setter
    private RoomStatus roomStatus;
    private final List<Amenity> amenities;
    private final List<Bed> beds;
    private final List<String> imageUrls;
    private final java.time.LocalDate createdAt;
    @Setter
    private boolean deleted;
    
    private Room(RoomId id, Integer roomNumber, RoomType roomType, 
                 RoomStatus roomStatus, List<Amenity> amenities, List<String> imageUrls, java.time.LocalDate createdAt) {
        this.id = Objects.requireNonNull(id, "Room ID cannot be null");
        this.roomNumber = Objects.requireNonNull(roomNumber, "Room number cannot be null");
        this.roomType = Objects.requireNonNull(roomType, "Room type cannot be null");
        this.roomStatus = Objects.requireNonNull(roomStatus, "Room status cannot be null");
        this.amenities = new ArrayList<>(amenities != null ? amenities : Collections.emptyList());
        this.beds = new ArrayList<>();
        this.imageUrls = new ArrayList<>(imageUrls != null ? imageUrls : Collections.emptyList());
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.deleted = false;
        
        validateRoomNumber(roomNumber);
    }
    
    public static Room create(Integer roomNumber, RoomType roomType, List<Amenity> amenities) {
        return create(roomNumber, roomType, amenities, null);
    }

    public static Room create(Integer roomNumber, RoomType roomType, List<Amenity> amenities, List<String> imageUrls) {
        RoomId id = RoomId.generate();
        return new Room(id, roomNumber, roomType, RoomStatus.AVAILABLE, amenities, imageUrls, java.time.LocalDate.now());
    }
    
    public static Room reconstitute(RoomId id, Integer roomNumber, RoomType roomType,
                                   RoomStatus roomStatus, List<Amenity> amenities, 
                                   List<Bed> beds, java.time.LocalDate createdAt) {
        return reconstitute(id, roomNumber, roomType, roomStatus, amenities, beds, createdAt, null, false);
    }

    public static Room reconstitute(RoomId id, Integer roomNumber, RoomType roomType,
                                   RoomStatus roomStatus, List<Amenity> amenities,
                                   List<Bed> beds, java.time.LocalDate createdAt, boolean deleted) {
        return reconstitute(id, roomNumber, roomType, roomStatus, amenities, beds, createdAt, null, deleted);
    }

    public static Room reconstitute(RoomId id, Integer roomNumber, RoomType roomType,
                                   RoomStatus roomStatus, List<Amenity> amenities,
                                   List<Bed> beds, java.time.LocalDate createdAt,
                                   List<String> imageUrls, boolean deleted) {
        Room room = new Room(id, roomNumber, roomType, roomStatus, amenities, imageUrls, createdAt);
        if (beds != null) {
            for (Bed bed : beds) {
                bed.setRoom(room);
            }
            room.beds.addAll(beds);
        }
        room.deleted = deleted;
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
    
    public void markAsDeleted() {
        this.deleted = true;
    }
    
    public void addAmenity(Amenity amenity) {
        if (!amenities.contains(amenity)) {
            amenities.add(amenity);
        }
    }
    
    public void removeAmenity(Amenity amenity) {
        amenities.remove(amenity);
    }
    
    public boolean isOutOfService() {
        return roomStatus == RoomStatus.OUT_OF_SERVICE;
    }

    public boolean isAvailable() {
        return roomStatus == RoomStatus.AVAILABLE;
    }
    
    public void addImageUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank() && !imageUrls.contains(imageUrl)) {
            imageUrls.add(imageUrl);
        }
    }

    public void removeImageUrl(String imageUrl) {
        imageUrls.remove(imageUrl);
    }

    public boolean canAccommodate(int numberOfGuests) {
        return roomType.getMaxOccupancy() >= numberOfGuests;
    }
    
    private void validateRoomNumber(Integer roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
    }
    
    // Custom getters for collections to return unmodifiable views
    public List<Amenity> getAmenities() {
        return Collections.unmodifiableList(amenities);
    }
    
    public List<Bed> getBeds() {
        return Collections.unmodifiableList(beds);
    }

    public List<String> getImageUrls() {
        return Collections.unmodifiableList(imageUrls);
    }
    
    // Other getters, equals, and hashCode are generated by Lombok
}
