package com.daboerp.gestion.infrastructure.persistence.entity;

import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA entity for Guest persistence.
 * Framework-specific, isolated from domain.
 */
@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestJpaEntity {
    
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false, length = 50)
    private Nationality nationality;
    
    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType;
    
    @Column(name = "notes_id", length = 36)
    private String notesId;
    
    @Column(name = "notes_text", columnDefinition = "TEXT")
    private String notesText;
    
    @Column(name = "notes_level", length = 20)
    private String notesLevel;
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}
