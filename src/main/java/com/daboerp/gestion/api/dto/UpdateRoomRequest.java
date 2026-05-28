package com.daboerp.gestion.api.dto;

import java.util.List;

public record UpdateRoomRequest(
    Integer roomNumber,
    String roomTypeId,
    List<String> amenities,
    Integer numberOfBeds,
    List<String> imageUrls
) {}
