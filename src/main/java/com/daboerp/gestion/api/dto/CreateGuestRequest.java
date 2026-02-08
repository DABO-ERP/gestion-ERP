package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO for creating a new guest.
 */
public record CreateGuestRequest(
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    String phone,
    
    LocalDate dateOfBirth,
    
    @NotNull(message = "Nationality is required")
    Nationality nationality,
    
    @NotBlank(message = "Document number is required")
    String documentNumber,
    
    @NotNull(message = "Document type is required")
    DocumentType documentType
) {}
