package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.Amenity;

import java.util.List;

public record UpdateRoomRequest(
    Integer roomNumber,
    String roomTypeId,
    List<Amenity> amenities,
    Integer numberOfBeds,
    List<String> imageUrls
) {}
