package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;

import java.time.LocalDate;

/**
 * DTO for guest response.
 */
public record GuestResponse(
    String id,
    String firstName,
    String lastName,
    String email,
    String phone,
    LocalDate dateOfBirth,
    Nationality nationality,
    String documentNumber,
    DocumentType documentType,
    String notes,
    LocalDate createdAt
) {}
