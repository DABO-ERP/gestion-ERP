package com.daboerp.gestion.api.dto;

/**
 * Response DTO for room conflict errors (e.g. deleted room exists).
 */
public record RoomConflictResponse(
    int status,
    String error,
    String message,
    String path,
    long timestamp,
    String existingRoomId,
    Integer roomNumber,
    String errorCode
) {}
