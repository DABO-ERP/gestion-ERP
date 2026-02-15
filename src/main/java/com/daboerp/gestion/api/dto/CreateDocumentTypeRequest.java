package com.daboerp.gestion.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new document type.
 */
public record CreateDocumentTypeRequest(
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code cannot exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code must contain only uppercase letters, numbers, and underscores")
    String code,
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,
    
    String description,
    
    @Pattern(regexp = "^.{0,255}$", message = "Validation regex cannot exceed 255 characters")
    String validationRegex,
    
    @NotNull(message = "Active status is required")
    Boolean active
) {}