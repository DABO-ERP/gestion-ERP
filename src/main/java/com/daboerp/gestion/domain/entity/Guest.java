package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.event.DomainEvent;
import com.daboerp.gestion.domain.event.guest.GuestCreatedEvent;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.Nationality;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Guest aggregate root.
 * Represents a guest in the hostel management system.
 * No framework dependencies - pure domain model.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Guest {
    
    @EqualsAndHashCode.Include
    private final GuestId id;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private LocalDate dateOfBirth;
    @Setter
    private Nationality nationality;
    @Setter
    private String documentNumber;
    @Setter
    private DocumentType documentType;
    @Setter
    private Notes notes;
    private final LocalDate createdAt;
    
    // Domain events
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    // Private constructor to enforce creation through factory method
    private Guest(GuestId id, String firstName, String lastName, String email, 
                  String phone, LocalDate dateOfBirth, Nationality nationality,
                  String documentNumber, DocumentType documentType, LocalDate createdAt) {
        this.id = Objects.requireNonNull(id, "Guest ID cannot be null");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.nationality = Objects.requireNonNull(nationality, "Nationality cannot be null");
        this.documentNumber = Objects.requireNonNull(documentNumber, "Document number cannot be null");
        this.documentType = Objects.requireNonNull(documentType, "Document type cannot be null");
        this.notes = null;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        
        validateEmail(email);
    }
    
    // Factory method for creating new guests
    public static Guest create(String firstName, String lastName, String email, 
                               String phone, LocalDate dateOfBirth, Nationality nationality,
                               String documentNumber, DocumentType documentType) {
        GuestId id = GuestId.generate();
        Guest guest = new Guest(id, firstName, lastName, email, phone, dateOfBirth, 
                        nationality, documentNumber, documentType, LocalDate.now());
        
        // Raise domain event
        guest.addDomainEvent(new GuestCreatedEvent(id, email, firstName, lastName));
        
        return guest;
    }
    
    // Reconstitution method for loading from persistence
    public static Guest reconstitute(GuestId id, String firstName, String lastName, String email,
                                     String phone, LocalDate dateOfBirth, Nationality nationality,
                                     String documentNumber, DocumentType documentType, 
                                     Notes notes, LocalDate createdAt) {
        Guest guest = new Guest(id, firstName, lastName, email, phone, dateOfBirth,
                               nationality, documentNumber, documentType, createdAt);
        guest.notes = notes;
        return guest;
    }
    
    // Business methods
    public void updateContactInfo(String email, String phone) {
        Objects.requireNonNull(email, "Email cannot be null");
        validateEmail(email);
        this.email = email;
        this.phone = phone;
    }
    
    public void updatePersonalInfo(String firstName, String lastName, LocalDate dateOfBirth) {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
    
    public void addNote(String text, LevelNote level) {
        this.notes = Notes.create(text, level);
    }
    
    public void updateNote(String text, LevelNote level) {
        if (this.notes == null) {
            throw new IllegalStateException("Cannot update note: no note exists");
        }
        this.notes = Notes.create(text, level);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    
    // Domain Events Management
    public void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
    
    // Getters, equals, and hashCode are generated by Lombok
}
