package com.daboerp.gestion.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    String id,
    String reservationId,
    BigDecimal amount,
    String method,
    LocalDateTime paidAt,
    String note,
    LocalDateTime createdAt
) {}
