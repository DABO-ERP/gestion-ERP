package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.PaymentMethod;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payment entity.
 * Represents a payment applied to a reservation.
 * No framework dependencies - pure domain model.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment {

    @EqualsAndHashCode.Include
    private final PaymentId id;
    private final ReservationId reservationId;
    private final BigDecimal amount;
    private final PaymentMethod method;
    private final LocalDateTime paidAt;
    private final String note;
    private final LocalDateTime createdAt;

    private Payment(PaymentId id, ReservationId reservationId, BigDecimal amount,
                    PaymentMethod method, LocalDateTime paidAt, String note, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Payment ID cannot be null");
        this.reservationId = Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.method = Objects.requireNonNull(method, "Payment method cannot be null");
        this.paidAt = Objects.requireNonNull(paidAt, "Paid at cannot be null");
        this.note = note;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    public static Payment create(ReservationId reservationId, BigDecimal amount,
                                 PaymentMethod method, String note) {
        PaymentId id = PaymentId.generate();
        LocalDateTime now = LocalDateTime.now();
        return new Payment(id, reservationId, amount, method, now, note, now);
    }

    public static Payment reconstitute(PaymentId id, ReservationId reservationId, BigDecimal amount,
                                       PaymentMethod method, LocalDateTime paidAt, String note,
                                       LocalDateTime createdAt) {
        return new Payment(id, reservationId, amount, method, paidAt, note, createdAt);
    }
}
