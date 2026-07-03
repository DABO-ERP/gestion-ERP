package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing room type.
 */
public record UpdateRoomTypeRequest(
    String name,
    String description,
    Integer maxOccupancy,
    BigDecimal basePrice
) {}
