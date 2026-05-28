package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAmenityDefinitionRequest(
    @NotBlank(message = "Name is required") String name
) {}
