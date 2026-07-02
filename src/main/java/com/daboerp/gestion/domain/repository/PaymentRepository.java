package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(PaymentId id);

    List<Payment> findByReservationId(ReservationId reservationId);

    List<Payment> findAll();
}
