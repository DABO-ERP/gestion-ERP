package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new room type.
 */
public record CreateRoomTypeRequest(
    @NotBlank(message = "Name is required")
    String name,
    
    String description,
    
    @NotNull(message = "Max occupancy is required")
    @Positive(message = "Max occupancy must be positive")
    Integer maxOccupancy,
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    BigDecimal basePrice
) {}
