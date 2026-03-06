package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.event.DomainEvent;
import com.daboerp.gestion.domain.valueobject.DocumentTypeId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Document Type aggregate root.
 * Represents a document type configuration in the hostel management system.
 * No framework dependencies - pure domain model.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DocumentTypeEntity {
    
    @EqualsAndHashCode.Include
    private final DocumentTypeId id;
    
    @Setter
    private String code;
    
    @Setter
    private String name;
    
    @Setter
    private String description;
    
    @Setter
    private String validationRegex;
    
    @Setter
    private boolean active;
    
    private final LocalDateTime createdAt;
    
    @Setter
    private LocalDateTime updatedAt;
    
    // Domain events
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    // Private constructor to enforce creation through factory method
    private DocumentTypeEntity(DocumentTypeId id, String code, String name, String description,
                              String validationRegex, boolean active, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Document type ID cannot be null");
        this.code = validateCode(code);
        this.name = validateName(name);
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.validationRegex = validationRegex;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = createdAt;
    }
    
    /**
     * Factory method to create a new document type.
     */
    public static DocumentTypeEntity create(String code, String name, String description,
                                           String validationRegex, boolean active) {
        return new DocumentTypeEntity(
            DocumentTypeId.generate(),
            code,
            name,
            description,
            validationRegex,
            active,
            LocalDateTime.now()
        );
    }
    
    /**
     * Factory method to reconstitute from persistence.
     */
    public static DocumentTypeEntity reconstitute(DocumentTypeId id, String code, String name, String description,
                                                 String validationRegex, boolean active, 
                                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        DocumentTypeEntity documentType = new DocumentTypeEntity(id, code, name, description, 
                                                                validationRegex, active, createdAt);
        documentType.updatedAt = updatedAt;
        return documentType;
    }
    
    /**
     * Validates a document number against this document type's regex pattern.
     */
    public boolean isValidDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            return false;
        }
        
        if (validationRegex == null || validationRegex.isBlank()) {
            return true; // No validation pattern means any format is valid
        }
        
        try {
            Pattern pattern = Pattern.compile(validationRegex);
            return pattern.matcher(documentNumber).matches();
        } catch (Exception e) {
            // If regex is invalid, consider document number as invalid
            return false;
        }
    }
    
    /**
     * Activate this document type.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Deactivate this document type.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Update document type details.
     */
    public void updateDetails(String name, String description, String validationRegex) {
        this.name = validateName(name);
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.validationRegex = validationRegex;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Get all domain events and clear them.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    /**
     * Clear domain events after publishing.
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }
    
    private String validateCode(String code) {
        Objects.requireNonNull(code, "Document type code cannot be null");
        if (code.isBlank()) {
            throw new IllegalArgumentException("Document type code cannot be blank");
        }
        if (code.length() > 20) {
            throw new IllegalArgumentException("Document type code cannot exceed 20 characters");
        }
        // Code should be uppercase alphanumeric
        if (!code.matches("^[A-Z0-9_]+$")) {
            throw new IllegalArgumentException("Document type code must contain only uppercase letters, numbers, and underscores");
        }
        return code;
    }
    
    private String validateName(String name) {
        Objects.requireNonNull(name, "Document type name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Document type name cannot be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Document type name cannot exceed 100 characters");
        }
        return name;
    }
    
    @Override
    public String toString() {
        return "DocumentTypeEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}