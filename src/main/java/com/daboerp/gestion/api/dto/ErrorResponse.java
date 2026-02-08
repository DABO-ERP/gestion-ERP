package com.daboerp.gestion.api.dto;

/**
 * Standard error response DTO.
 */
public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    long timestamp
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, System.currentTimeMillis());
    }
}
