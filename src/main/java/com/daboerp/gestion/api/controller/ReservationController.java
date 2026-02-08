package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.CreateReservationRequest;
import com.daboerp.gestion.api.dto.ReservationResponse;
import com.daboerp.gestion.application.usecase.reservation.*;
import com.daboerp.gestion.domain.entity.Reservation;
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
    public ResponseEntity<List<ReservationResponse>> listReservations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String filter) {
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
    public ResponseEntity<ReservationResponse> checkIn(@PathVariable String id) {
        var command = new CheckInReservationUseCase.CheckInCommand(id, LocalDate.now());
        Reservation reservation = checkInReservationUseCase.execute(command);
        return ResponseEntity.ok(toResponse(reservation));
    }
    
    @PostMapping("/{id}/check-out")
    public ResponseEntity<ReservationResponse> checkOut(@PathVariable String id) {
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
