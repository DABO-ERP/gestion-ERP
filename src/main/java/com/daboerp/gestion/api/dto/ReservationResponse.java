package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for reservation response.
 */
public record ReservationResponse(
    String id,
    String reservationCode,
    LocalDate checkIn,
    LocalDate checkOut,
    String status,
    BigDecimal quotedAmount,
    String source,
    String guestPrincipalId,
    String guestPrincipalName,
    String roomId,
    Integer roomNumber,
    LocalDate createdAt
) {}
