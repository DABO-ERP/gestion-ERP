package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.PaymentMethod;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.math.BigDecimal;
import java.util.Objects;

public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public RegisterPaymentUseCase(PaymentRepository paymentRepository,
                                  ReservationRepository reservationRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "Payment repository cannot be null");
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
    }

    public Payment execute(RegisterPaymentCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        ReservationId reservationId = ReservationId.of(command.reservationId());
        reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", command.reservationId()));

        Payment payment = Payment.create(reservationId, command.amount(), command.method(), command.note());
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
            Objects.requireNonNull(method, "Payment method cannot be null");
        }
    }
}
