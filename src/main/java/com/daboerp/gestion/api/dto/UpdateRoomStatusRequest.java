package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for updating a room's operational status.
 */
public record UpdateRoomStatusRequest(
    @NotBlank(message = "Status is required")
    String status
) {}
