package com.daboerp.gestion.api.dto;

import java.time.LocalDate;

public record RoomBlockResponse(
    String id,
    String roomId,
    Integer roomNumber,
    LocalDate startDate,
    LocalDate endDate,
    String reason,
    String createdAt
) {}
