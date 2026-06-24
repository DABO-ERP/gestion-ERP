package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.application.exception.BusinessRuleViolationException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.Objects;

public class VoidPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final com.daboerp.gestion.domain.repository.ReservationRepository reservationRepository;

    public VoidPaymentUseCase(PaymentRepository paymentRepository,
                              com.daboerp.gestion.domain.repository.ReservationRepository reservationRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "Payment repository cannot be null");
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
    }

    public void execute(VoidPaymentCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        ReservationId reservationId = ReservationId.of(command.reservationId());
        reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", command.reservationId()));

        PaymentId paymentId = PaymentId.of(command.paymentId());
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment", command.paymentId()));

        if (!payment.getReservationId().equals(reservationId)) {
            throw new BusinessRuleViolationException(
                "Payment " + command.paymentId() + " does not belong to reservation " + command.reservationId()
            );
        }

        paymentRepository.delete(paymentId);
    }

    public record VoidPaymentCommand(String paymentId, String reservationId) {
        public VoidPaymentCommand {
            Objects.requireNonNull(paymentId, "Payment ID cannot be null");
            Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        }
    }
}
