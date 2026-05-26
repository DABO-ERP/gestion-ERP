package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;

public record UpdateRoomTypeRequest(
    String name,
    String description,
    Integer maxOccupancy,
    BigDecimal basePrice
) {}
