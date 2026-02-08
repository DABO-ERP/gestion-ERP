package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.Amenity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for creating a new room.
 */
public record CreateRoomRequest(
    @NotNull(message = "Room number is required")
    Integer roomNumber,
    
    @NotNull(message = "Room type ID is required")
    String roomTypeId,
    
    List<Amenity> amenities,
    
    Integer numberOfBeds
) {}
