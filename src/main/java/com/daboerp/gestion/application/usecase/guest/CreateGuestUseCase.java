package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Use case for creating a new guest.
 * No framework dependencies - pure application logic.
 */
public class CreateGuestUseCase {
    
    private final GuestRepository guestRepository;
    
    public CreateGuestUseCase(GuestRepository guestRepository) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
    }
    
    public Guest execute(CreateGuestCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        // Validate guest doesn't already exist
        if (guestRepository.existsByEmail(command.email())) {
            throw new ResourceAlreadyExistsException("Guest", command.email());
        }
        
        // Create guest domain entity
        Guest guest = Guest.create(
            command.firstName(),
            command.lastName(),
            command.email(),
            command.phone(),
            command.dateOfBirth(),
            command.nationality(),
            command.documentNumber(),
            command.documentType()
        );
        
        // Persist guest
        return guestRepository.save(guest);
    }
    
    public record CreateGuestCommand(
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Nationality nationality,
        String documentNumber,
        DocumentType documentType
    ) {
        public CreateGuestCommand {
            Objects.requireNonNull(firstName, "First name cannot be null");
            Objects.requireNonNull(lastName, "Last name cannot be null");
            Objects.requireNonNull(email, "Email cannot be null");
            Objects.requireNonNull(nationality, "Nationality cannot be null");
            Objects.requireNonNull(documentNumber, "Document number cannot be null");
            Objects.requireNonNull(documentType, "Document type cannot be null");
        }
    }
}
