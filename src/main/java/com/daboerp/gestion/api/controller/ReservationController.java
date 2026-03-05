package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.CreateReservationRequest;
import com.daboerp.gestion.api.dto.ReservationResponse;
import com.daboerp.gestion.application.usecase.reservation.*;
import com.daboerp.gestion.domain.entity.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for reservation management.
 */
@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation Management", description = "APIs for managing reservations, check-ins, and check-outs")
public class ReservationController {
    
    private final CreateReservationUseCase createReservationUseCase;
    private final ListReservationsUseCase listReservationsUseCase;
    private final CheckInReservationUseCase checkInReservationUseCase;
    private final CheckOutReservationUseCase checkOutReservationUseCase;
    
    public ReservationController(CreateReservationUseCase createReservationUseCase,
                                ListReservationsUseCase listReservationsUseCase,
                                CheckInReservationUseCase checkInReservationUseCase,
                                CheckOutReservationUseCase checkOutReservationUseCase) {
        this.createReservationUseCase = createReservationUseCase;
        this.listReservationsUseCase = listReservationsUseCase;
        this.checkInReservationUseCase = checkInReservationUseCase;
        this.checkOutReservationUseCase = checkOutReservationUseCase;
    }
    
    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Book a room for a guest")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reservation created successfully",
            content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Guest or room not found"),
        @ApiResponse(responseCode = "422", description = "Room not available for selected dates")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        var command = new CreateReservationUseCase.CreateReservationCommand(
            request.checkIn(),
            request.checkOut(),
            request.quotedAmount(),
            request.source(),
            request.guestPrincipalId(),
            request.roomId(),
            request.additionalGuestIds()
        );
        
        Reservation reservation = createReservationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(reservation));
    }
    
    @GetMapping
    @Operation(summary = "List reservations", description = "Get all reservations with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<ReservationResponse>> listReservations(
            @Parameter(description = "Start date for filtering (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date for filtering (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Filter: 'active' for active reservations only") @RequestParam(required = false) String filter) {
        List<Reservation> reservations;
        
        if ("active".equalsIgnoreCase(filter)) {
            reservations = listReservationsUseCase.executeActive();
        } else if (startDate != null && endDate != null) {
            reservations = listReservationsUseCase.executeByDateRange(startDate, endDate);
        } else {
            reservations = listReservationsUseCase.execute();
        }
        
        List<ReservationResponse> response = reservations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/check-in")
    @Operation(summary = "Check-in a reservation", description = "Mark a reservation as checked in")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check-in successful",
            content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "422", description = "Invalid reservation state for check-in")
    })
    public ResponseEntity<ReservationResponse> checkIn(
            @Parameter(description = "Reservation ID") @PathVariable String id) {
        var command = new CheckInReservationUseCase.CheckInCommand(id, LocalDate.now());
        Reservation reservation = checkInReservationUseCase.execute(command);
        return ResponseEntity.ok(toResponse(reservation));
    }
    
    @PostMapping("/{id}/check-out")
    @Operation(summary = "Check-out a reservation", description = "Mark a reservation as checked out")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check-out successful",
            content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "422", description = "Invalid reservation state for check-out")
    })
    public ResponseEntity<ReservationResponse> checkOut(
            @Parameter(description = "Reservation ID") @PathVariable String id) {
        var command = new CheckOutReservationUseCase.CheckOutCommand(id, LocalDate.now());
        Reservation reservation = checkOutReservationUseCase.execute(command);
        return ResponseEntity.ok(toResponse(reservation));
    }
    
    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId().getValue(),
            reservation.getReservationCode(),
            reservation.getCheckIn(),
            reservation.getCheckOut(),
            reservation.getStatus().getStatusType().name(),
            reservation.getQuotedAmount(),
            reservation.getSource().name(),
            reservation.getGuestPrincipal().getId().getValue(),
            reservation.getGuestPrincipal().getFullName(),
            reservation.getRoom().getId().getValue(),
            reservation.getRoom().getRoomNumber(),
            reservation.getCreatedAt()
        );
    }
}
