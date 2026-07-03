package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request DTO for blocking a room for a date range.
 */
public record BlockRoomRequest(
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    String reason
) {}
