package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.time.LocalDate;
import java.util.List;

public record RoomResponse(
    String id,
    Integer roomNumber,
    RoomTypeResponse roomType,
    RoomStatus roomStatus,
    List<Amenity> amenities,
    Integer bedCount,
    List<String> imageUrls,
    LocalDate createdAt
) {}
