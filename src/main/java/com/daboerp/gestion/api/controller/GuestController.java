package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.CreateGuestRequest;
import com.daboerp.gestion.api.dto.GuestResponse;
import com.daboerp.gestion.application.usecase.guest.CreateGuestUseCase;
import com.daboerp.gestion.application.usecase.guest.GetGuestUseCase;
import com.daboerp.gestion.application.usecase.guest.ListGuestsUseCase;
import com.daboerp.gestion.domain.entity.Guest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Guest Management", description = "APIs for managing guests and customer information")
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
    @Operation(summary = "Create a new guest", description = "Register a new guest in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Guest created successfully",
            content = @Content(schema = @Schema(implementation = GuestResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Guest already exists with this email")
    })
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
    @Operation(summary = "Get guest by ID", description = "Retrieve detailed information about a specific guest")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Guest found",
            content = @Content(schema = @Schema(implementation = GuestResponse.class))),
        @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    public ResponseEntity<GuestResponse> getGuest(
            @Parameter(description = "Guest ID") @PathVariable String id) {
        Guest guest = getGuestUseCase.execute(id);
        return ResponseEntity.ok(toResponse(guest));
    }
    
    @GetMapping
    @Operation(summary = "List all guests", description = "Get a list of all guests or search by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<GuestResponse>> listGuests(
            @Parameter(description = "Search term for guest name") @RequestParam(required = false) String search) {
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
