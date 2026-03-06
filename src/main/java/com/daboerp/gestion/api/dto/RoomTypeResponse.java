package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;

/**
 * Response DTO for room type data.
 */
public record RoomTypeResponse(
    String id,
    String name,
    String description,
    Integer maxOccupancy,
    BigDecimal basePrice
) {}
