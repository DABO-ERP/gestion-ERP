package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateRoomRequest(
    @NotNull(message = "Room number is required")
    Integer roomNumber,

    @NotNull(message = "Room type ID is required")
    String roomTypeId,

    List<String> amenities,

    Integer numberOfBeds,

    List<String> imageUrls
) {}
