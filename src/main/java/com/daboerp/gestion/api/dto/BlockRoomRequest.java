package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BlockRoomRequest(
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    LocalDate startDate,

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    LocalDate endDate,

    String reason
) {}
