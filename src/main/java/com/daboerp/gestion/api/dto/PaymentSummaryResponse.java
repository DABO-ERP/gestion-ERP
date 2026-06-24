package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSummaryResponse(
    String id,
    String reservationId,
    String reservationCode,
    String guestPrincipalName,
    BigDecimal amount,
    String method,
    LocalDateTime paidAt,
    String note,
    LocalDateTime createdAt
) {}
