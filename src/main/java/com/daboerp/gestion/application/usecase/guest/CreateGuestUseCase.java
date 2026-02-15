package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.application.command.CommandHandler;
import com.daboerp.gestion.application.command.guest.CreateGuestCommand;
import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.event.DomainEventPublisher;
import com.daboerp.gestion.domain.repository.GuestRepository;

import java.util.Objects;

/**
 * Use case for creating a new guest.
 * Implements Command Handler pattern for better separation of concerns.
 * No framework dependencies - pure application logic.
 */
public class CreateGuestUseCase implements CommandHandler<CreateGuestCommand, Guest> {
    
    private final GuestRepository guestRepository;
    private final DomainEventPublisher eventPublisher;
    
    public CreateGuestUseCase(GuestRepository guestRepository, DomainEventPublisher eventPublisher) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "Event publisher cannot be null");
    }
    
    @Override
    public Guest handle(CreateGuestCommand command) {
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
        Guest savedGuest = guestRepository.save(guest);
        
        // Publish domain events
        eventPublisher.publishAll(savedGuest.getDomainEvents());
        savedGuest.clearDomainEvents();
        
        return savedGuest;
    }
    
    @Override
    public String getSupportedCommandType() {
        return "CreateGuest";
    }
}
