package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Use case for updating guest information.
 */
public class UpdateGuestUseCase {
    
    private final GuestRepository guestRepository;
    
    public UpdateGuestUseCase(GuestRepository guestRepository) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
    }
    
    public Guest execute(UpdateGuestCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        GuestId id = GuestId.of(command.guestId());
        
        Guest guest = guestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guest", command.guestId()));
        
        // Update guest information
        guest.updateContactInfo(command.email(), command.phone());
        guest.updatePersonalInfo(command.firstName(), command.lastName(), command.dateOfBirth());
        
        return guestRepository.save(guest);
    }
    
    public record UpdateGuestCommand(
        String guestId,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth
    ) {
        public UpdateGuestCommand {
            Objects.requireNonNull(guestId, "Guest ID cannot be null");
            Objects.requireNonNull(firstName, "First name cannot be null");
            Objects.requireNonNull(lastName, "Last name cannot be null");
            Objects.requireNonNull(email, "Email cannot be null");
        }
    }
}
