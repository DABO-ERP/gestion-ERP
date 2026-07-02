package com.daboerp.gestion.application.usecase.payment;

import com.daboerp.gestion.application.exception.BusinessRuleViolationException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.Objects;

public class VoidPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public VoidPaymentUseCase(PaymentRepository paymentRepository,
                              ReservationRepository reservationRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
    }

    public void execute(String paymentId) {
        Objects.requireNonNull(paymentId);

        PaymentId id = PaymentId.of(paymentId);
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        var reservation = reservationRepository.findById(payment.getReservationId())
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", payment.getReservationId().getValue()));

        if (reservation.getStatus().getStatusType() == StatusType.CHECKED_IN ||
            reservation.getStatus().getStatusType() == StatusType.CHECKED_OUT) {
            throw new BusinessRuleViolationException(
                "Cannot void payment after check-in. Reservation " + reservation.getReservationCode() +
                " is already " + reservation.getStatus().getStatusType().getDisplayName());
        }

        payment.markAsVoided();
        paymentRepository.save(payment);
    }
}
