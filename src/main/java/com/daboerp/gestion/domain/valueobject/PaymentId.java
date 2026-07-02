package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class PaymentId {

    private final String value;

    private PaymentId(String value) {
        Objects.requireNonNull(value, "Payment ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be blank");
        }
        this.value = value;
    }

    public static PaymentId of(String value) {
        return new PaymentId(value);
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentId that = (PaymentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
