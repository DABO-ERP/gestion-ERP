package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.Source;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating a new reservation.
 */
public record CreateReservationRequest(
    @NotNull(message = "Check-in date is required")
    LocalDate checkIn,
    
    @NotNull(message = "Check-out date is required")
    LocalDate checkOut,
    
    @NotNull(message = "Quoted amount is required")
    BigDecimal quotedAmount,
    
    @NotNull(message = "Source is required")
    Source source,
    
    @NotBlank(message = "Guest principal ID is required")
    String guestPrincipalId,
    
    @NotBlank(message = "Room ID is required")
    String roomId,
    
    List<String> additionalGuestIds
) {}
