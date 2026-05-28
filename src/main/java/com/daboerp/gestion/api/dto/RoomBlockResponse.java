package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.time.LocalDate;

public record RoomBlockResponse(
    String id,
    String roomId,
    Integer roomNumber,
    LocalDate startDate,
    LocalDate endDate,
    String reason,
    boolean active,
    LocalDate createdAt
) {}
