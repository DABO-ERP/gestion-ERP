package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.repository.PaymentRepository;

import java.util.List;
import java.util.Objects;

public class ListAllPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    public ListAllPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "Payment repository cannot be null");
    }

    public List<Payment> execute() {
        return paymentRepository.findAll();
    }
}
