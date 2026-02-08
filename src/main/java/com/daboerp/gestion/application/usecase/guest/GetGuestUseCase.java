package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;

import java.util.Objects;

/**
 * Use case for retrieving a guest by ID.
 */
public class GetGuestUseCase {
    
    private final GuestRepository guestRepository;
    
    public GetGuestUseCase(GuestRepository guestRepository) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
    }
    
    public Guest execute(String guestId) {
        Objects.requireNonNull(guestId, "Guest ID cannot be null");
        
        GuestId id = GuestId.of(guestId);
        
        return guestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guest", guestId));
    }
}
