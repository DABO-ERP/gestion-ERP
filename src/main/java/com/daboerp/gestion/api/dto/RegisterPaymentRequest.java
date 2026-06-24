package com.daboerp.gestion.api.dto;

import com.daboerp.gestion.domain.valueobject.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RegisterPaymentRequest(
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,

    @NotNull(message = "Payment method is required")
    PaymentMethod method,

    String note
) {}
