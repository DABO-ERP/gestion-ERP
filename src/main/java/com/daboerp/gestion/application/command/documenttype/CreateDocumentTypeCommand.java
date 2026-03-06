package com.daboerp.gestion.application.command.documenttype;

import com.daboerp.gestion.application.command.Command;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to create a new document type.
 * Immutable command object following CQRS pattern.
 */
public record CreateDocumentTypeCommand(
    UUID commandId,
    String code,
    String name,
    String description,
    String validationRegex,
    boolean active
) implements Command {
    
    public CreateDocumentTypeCommand {
        Objects.requireNonNull(commandId, "Command ID cannot be null");
        Objects.requireNonNull(code, "Code cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        
        if (code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be blank");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (code.length() > 20) {
            throw new IllegalArgumentException("Code cannot exceed 20 characters");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }
        // Validate code format
        if (!code.matches("^[A-Z0-9_]+$")) {
            throw new IllegalArgumentException("Code must contain only uppercase letters, numbers, and underscores");
        }
    }
    
    @Override
    public UUID getCommandId() {
        return commandId;
    }
    
    @Override
    public String getCommandType() {
        return "CreateDocumentType";
    }
    
    /**
     * Factory method for creating with generated command ID.
     */
    public static CreateDocumentTypeCommand create(String code, String name, String description,
                                                  String validationRegex, boolean active) {
        return new CreateDocumentTypeCommand(UUID.randomUUID(), code, name, description, validationRegex, active);
    }
}