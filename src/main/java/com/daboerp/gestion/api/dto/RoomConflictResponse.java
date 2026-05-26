package com.daboerp.gestion.api.dto;

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
