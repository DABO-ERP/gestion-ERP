package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.CreateGuestRequest;
import com.daboerp.gestion.api.dto.GuestResponse;
import com.daboerp.gestion.application.usecase.guest.CreateGuestUseCase;
import com.daboerp.gestion.application.usecase.guest.GetGuestUseCase;
import com.daboerp.gestion.application.usecase.guest.ListGuestsUseCase;
import com.daboerp.gestion.domain.entity.Guest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for guest management.
 */
@RestController
@RequestMapping("/api/v1/guests")
public class GuestController {
    
    private final CreateGuestUseCase createGuestUseCase;
    private final GetGuestUseCase getGuestUseCase;
    private final ListGuestsUseCase listGuestsUseCase;
    
    public GuestController(CreateGuestUseCase createGuestUseCase,
                          GetGuestUseCase getGuestUseCase,
                          ListGuestsUseCase listGuestsUseCase) {
        this.createGuestUseCase = createGuestUseCase;
        this.getGuestUseCase = getGuestUseCase;
        this.listGuestsUseCase = listGuestsUseCase;
    }
    
    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody CreateGuestRequest request) {
        var command = new CreateGuestUseCase.CreateGuestCommand(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.dateOfBirth(),
            request.nationality(),
            request.documentNumber(),
            request.documentType()
        );
        
        Guest guest = createGuestUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(guest));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuest(@PathVariable String id) {
        Guest guest = getGuestUseCase.execute(id);
        return ResponseEntity.ok(toResponse(guest));
    }
    
    @GetMapping
    public ResponseEntity<List<GuestResponse>> listGuests(
            @RequestParam(required = false) String search) {
        List<Guest> guests;
        if (search != null && !search.isBlank()) {
            guests = listGuestsUseCase.executeSearch(search);
        } else {
            guests = listGuestsUseCase.execute();
        }
        
        List<GuestResponse> response = guests.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    private GuestResponse toResponse(Guest guest) {
        String notes = guest.getNotes() != null ? guest.getNotes().getText() : null;
        return new GuestResponse(
            guest.getId().getValue(),
            guest.getFirstName(),
            guest.getLastName(),
            guest.getEmail(),
            guest.getPhone(),
            guest.getDateOfBirth(),
            guest.getNationality(),
            guest.getDocumentNumber(),
            guest.getDocumentType(),
            notes,
            guest.getCreatedAt()
        );
    }
}
