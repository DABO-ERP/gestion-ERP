package com.daboerp.gestion.api.dto;

import java.util.List;

/**
 * Request DTO for updating an existing room.
 */
public record UpdateRoomRequest(
    Integer roomNumber,
    String roomTypeId,
    List<String> amenities,
    Integer numberOfBeds,
    List<String> imageUrls
) {}
