package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating a new amenity definition.
 */
public record CreateAmenityDefinitionRequest(
    @NotBlank(message = "Name is required") String name
) {}
