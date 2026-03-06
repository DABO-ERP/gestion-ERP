package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * DTO for updating guest information.
 */
public record UpdateGuestRequest(
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    String phone,
    
    LocalDate dateOfBirth
) {}
