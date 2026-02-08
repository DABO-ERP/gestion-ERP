package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.Nationality;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Guest aggregate root.
 * Represents a guest in the hostel management system.
 * No framework dependencies - pure domain model.
 */
public class Guest {
    
    private final GuestId id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Nationality nationality;
    private String documentNumber;
    private DocumentType documentType;
    private Notes notes;
    private final LocalDate createdAt;
    
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
        return new Guest(id, firstName, lastName, email, phone, dateOfBirth, 
                        nationality, documentNumber, documentType, LocalDate.now());
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
    
    // Getters
    public GuestId getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public Nationality getNationality() {
        return nationality;
    }
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public DocumentType getDocumentType() {
        return documentType;
    }
    
    public Notes getNotes() {
        return notes;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(id, guest.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
