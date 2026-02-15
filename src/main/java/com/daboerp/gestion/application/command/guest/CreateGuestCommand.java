package com.daboerp.gestion.application.command.guest;

import com.daboerp.gestion.application.command.Command;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Command to create a new guest.
 * Immutable command object following CQRS pattern.
 */
public record CreateGuestCommand(
    UUID commandId,
    String firstName,
    String lastName,
    String email,
    String phone,
    LocalDate dateOfBirth,
    Nationality nationality,
    String documentNumber,
    DocumentType documentType
) implements Command {
    
    public CreateGuestCommand {
        Objects.requireNonNull(commandId, "Command ID cannot be null");
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(nationality, "Nationality cannot be null");
        Objects.requireNonNull(documentNumber, "Document number cannot be null");
        Objects.requireNonNull(documentType, "Document type cannot be null");
        
        if (firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
    }
    
    @Override
    public UUID getCommandId() {
        return commandId;
    }
    
    @Override
    public String getCommandType() {
        return "CreateGuest";
    }
    
    /**
     * Factory method for easier command creation.
     */
    public static CreateGuestCommand create(String firstName, String lastName, String email,
                                          String phone, LocalDate dateOfBirth, Nationality nationality,
                                          String documentNumber, DocumentType documentType) {
        return new CreateGuestCommand(
            UUID.randomUUID(), firstName, lastName, email, phone, 
            dateOfBirth, nationality, documentNumber, documentType
        );
    }
}