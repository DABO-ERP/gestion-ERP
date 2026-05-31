package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BlockRoomRequest(
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    String reason
) {}
