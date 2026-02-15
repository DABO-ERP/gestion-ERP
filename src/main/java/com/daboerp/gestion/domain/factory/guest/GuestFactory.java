package com.daboerp.gestion.domain.factory.guest;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.factory.EntityFactory;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.Nationality;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Factory for creating Guest entities with enhanced validation and consistency.
 * Encapsulates guest creation business rules.
 */
public class GuestFactory implements EntityFactory<Guest, GuestFactory.GuestBuilder> {
    
    @Override
    public Guest create(GuestBuilder builder) {
        validate(builder);
        return Guest.create(
            builder.firstName,
            builder.lastName, 
            builder.email,
            builder.phone,
            builder.dateOfBirth,
            builder.nationality,
            builder.documentNumber,
            builder.documentType
        );
    }
    
    @Override
    public Guest reconstitute(GuestBuilder builder) {
        validate(builder);
        return Guest.reconstitute(
            builder.id,
            builder.firstName,
            builder.lastName,
            builder.email,
            builder.phone,
            builder.dateOfBirth,
            builder.nationality,
            builder.documentNumber,
            builder.documentType,
            builder.notes,
            builder.createdAt
        );
    }
    
    @Override
    public void validate(GuestBuilder builder) {
        Objects.requireNonNull(builder.firstName, "First name cannot be null");
        Objects.requireNonNull(builder.lastName, "Last name cannot be null");
        Objects.requireNonNull(builder.email, "Email cannot be null");
        Objects.requireNonNull(builder.nationality, "Nationality cannot be null");
        Objects.requireNonNull(builder.documentNumber, "Document number cannot be null");
        Objects.requireNonNull(builder.documentType, "Document type cannot be null");
        
        validateAge(builder.dateOfBirth);
        validateEmailFormat(builder.email);
        validateDocumentNumber(builder.documentNumber, builder.documentType);
    }
    
    private void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now().minusYears(16))) {
            throw new IllegalArgumentException("Guest must be at least 16 years old");
        }
    }
    
    private void validateEmailFormat(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    
    private void validateDocumentNumber(String documentNumber, DocumentType documentType) {
        if (documentNumber == null || documentType == null) return;
        
        // Add specific validation rules based on document type
        switch (documentType) {
            case PASSPORT -> {
                if (documentNumber.length() < 6 || documentNumber.length() > 12) {
                    throw new IllegalArgumentException("Passport number must be between 6 and 12 characters");
                }
            }
            case NATIONAL_ID -> {
                if (documentNumber.length() < 8 || documentNumber.length() > 15) {
                    throw new IllegalArgumentException("National ID must be between 8 and 15 characters");
                }
            }
        }
    }
    
    /**
     * Builder for Guest creation with factory pattern.
     */
    public static class GuestBuilder {
        private GuestId id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private Nationality nationality;
        private String documentNumber;
        private DocumentType documentType;
        private com.daboerp.gestion.domain.entity.Notes notes;
        private LocalDate createdAt;
        
        public GuestBuilder id(GuestId id) {
            this.id = id;
            return this;
        }
        
        public GuestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public GuestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public GuestBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public GuestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public GuestBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        
        public GuestBuilder nationality(Nationality nationality) {
            this.nationality = nationality;
            return this;
        }
        
        public GuestBuilder documentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }
        
        public GuestBuilder documentType(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }
        
        public GuestBuilder notes(com.daboerp.gestion.domain.entity.Notes notes) {
            this.notes = notes;
            return this;
        }
        
        public GuestBuilder createdAt(LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }
    }
    
    /**
     * Static factory method for builder creation.
     */
    public static GuestBuilder builder() {
        return new GuestBuilder();
    }
}