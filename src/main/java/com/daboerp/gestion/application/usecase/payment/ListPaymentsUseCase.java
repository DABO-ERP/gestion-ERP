package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.List;
import java.util.Objects;

public class ListPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    public ListPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "Payment repository cannot be null");
    }

    public List<Payment> execute(ListPaymentsCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        ReservationId reservationId = ReservationId.of(command.reservationId());
        return paymentRepository.findByReservationId(reservationId);
    }

    public record ListPaymentsCommand(String reservationId) {
        public ListPaymentsCommand {
            Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        }
    }
}
