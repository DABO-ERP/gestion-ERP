package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for registering a new payment.
 */
public record RegisterPaymentRequest(
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal amount,

    @NotNull(message = "Method is required")
    PaymentMethod method,

    String note
) {}
