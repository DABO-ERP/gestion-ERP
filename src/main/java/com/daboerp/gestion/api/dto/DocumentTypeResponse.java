package com.daboerp.gestion.api.dto;

import java.time.LocalDateTime;

/**
 * DTO for document type responses.
 */
public record DocumentTypeResponse(
    String id,
    String code,
    String name,
    String description,
    String validationRegex,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}