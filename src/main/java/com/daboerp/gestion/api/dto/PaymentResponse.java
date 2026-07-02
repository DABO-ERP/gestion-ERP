package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    String id,
    String reservationId,
    BigDecimal amount,
    PaymentMethod method,
    String note,
    LocalDateTime paidAt,
    LocalDateTime createdAt,
    boolean voided
) {}
