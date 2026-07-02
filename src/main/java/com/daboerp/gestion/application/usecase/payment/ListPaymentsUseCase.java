package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.List;
import java.util.Objects;

public class ListPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    public ListPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    public List<Payment> executeByReservation(String reservationId) {
        Objects.requireNonNull(reservationId);
        return paymentRepository.findByReservationId(ReservationId.of(reservationId));
    }

    public List<Payment> executeAll() {
        return paymentRepository.findAll();
    }
}
