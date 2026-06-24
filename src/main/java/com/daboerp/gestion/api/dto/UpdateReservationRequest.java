package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.Source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateReservationRequest(
    LocalDate checkIn,
    LocalDate checkOut,
    BigDecimal quotedAmount,
    Source source,
    String guestPrincipalId,
    String roomId,
    List<String> additionalGuestIds
) {}
