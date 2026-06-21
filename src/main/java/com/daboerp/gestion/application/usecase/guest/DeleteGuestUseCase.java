package com.daboerp.gestion.application.usecase.guest;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;

import java.util.Objects;

public class DeleteGuestUseCase {

    private final GuestRepository guestRepository;

    public DeleteGuestUseCase(GuestRepository guestRepository) {
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
    }

    public void execute(String id) {
        Objects.requireNonNull(id, "Guest ID cannot be null");

        GuestId guestId = GuestId.of(id);

        guestRepository.findById(guestId)
            .orElseThrow(() -> new ResourceNotFoundException("Guest", id));

        guestRepository.delete(guestId);
    }
}
