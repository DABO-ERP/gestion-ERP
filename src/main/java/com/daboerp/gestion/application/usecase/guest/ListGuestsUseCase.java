package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.repository.GuestRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for listing all guests.
 */
public class ListGuestsUseCase {
    
    private final GuestRepository guestRepository;
    
    public ListGuestsUseCase(GuestRepository guestRepository) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
    }
    
    public List<Guest> execute() {
        return guestRepository.findAll();
    }
    
    public List<Guest> executeSearch(String name) {
        if (name == null || name.isBlank()) {
            return execute();
        }
        return guestRepository.searchByName(name);
    }
}
