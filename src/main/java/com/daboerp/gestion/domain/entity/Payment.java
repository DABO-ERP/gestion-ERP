package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

    private final PaymentId id;
    private final ReservationId reservationId;
    private BigDecimal amount;
    private PaymentMethod method;
    private String note;
    private final LocalDateTime paidAt;
    private final LocalDateTime createdAt;
    private boolean voided;

    private Payment(PaymentId id, ReservationId reservationId, BigDecimal amount,
                    PaymentMethod method, String note, LocalDateTime paidAt,
                    LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Payment ID cannot be null");
        this.reservationId = Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.method = Objects.requireNonNull(method, "Method cannot be null");
        this.note = note;
        this.paidAt = Objects.requireNonNull(paidAt, "Paid at cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.voided = false;

        validateAmount(amount);
    }

    public static Payment create(ReservationId reservationId, BigDecimal amount,
                                  PaymentMethod method, String note) {
        PaymentId id = PaymentId.generate();
        LocalDateTime now = LocalDateTime.now();
        return new Payment(id, reservationId, amount, method, note, now, now);
    }

    public static Payment reconstitute(PaymentId id, ReservationId reservationId,
                                        BigDecimal amount, PaymentMethod method,
                                        String note, LocalDateTime paidAt,
                                        LocalDateTime createdAt, boolean voided) {
        Payment payment = new Payment(id, reservationId, amount, method, note, paidAt, createdAt);
        payment.voided = voided;
        return payment;
    }

    public void markAsVoided() {
        if (this.voided) {
            throw new IllegalStateException("Payment is already voided");
        }
        this.voided = true;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public PaymentId getId() {
        return id;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isVoided() {
        return voided;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
