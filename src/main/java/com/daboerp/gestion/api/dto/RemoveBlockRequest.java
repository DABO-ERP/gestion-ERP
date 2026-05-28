package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RemoveBlockRequest(
    @NotBlank(message = "Block ID is required")
    String blockId
) {}
