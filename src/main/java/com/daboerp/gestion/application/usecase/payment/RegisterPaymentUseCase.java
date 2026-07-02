package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.entity.PaymentMethod;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.math.BigDecimal;
import java.util.Objects;

public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public RegisterPaymentUseCase(PaymentRepository paymentRepository,
                                  ReservationRepository reservationRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
    }

    public Payment execute(RegisterPaymentCommand command) {
        Objects.requireNonNull(command);

        ReservationId reservationId = ReservationId.of(command.reservationId());
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", command.reservationId()));

        BigDecimal totalPaid = paymentRepository.findByReservationId(reservationId).stream()
            .filter(p -> !p.isVoided())
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newTotal = totalPaid.add(command.amount());
        if (newTotal.compareTo(reservation.getQuotedAmount()) > 0) {
            throw new IllegalArgumentException(
                "Payment exceeds remaining balance. Total paid would be " + newTotal +
                " but quoted amount is " + reservation.getQuotedAmount());
        }

        Payment payment = Payment.create(
            reservationId,
            command.amount(),
            command.method(),
            command.note()
        );

        return paymentRepository.save(payment);
    }

    public record RegisterPaymentCommand(
        String reservationId,
        BigDecimal amount,
        PaymentMethod method,
        String note
    ) {
        public RegisterPaymentCommand {
            Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
            Objects.requireNonNull(amount, "Amount cannot be null");
            Objects.requireNonNull(method, "Method cannot be null");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
        }
    }
}
