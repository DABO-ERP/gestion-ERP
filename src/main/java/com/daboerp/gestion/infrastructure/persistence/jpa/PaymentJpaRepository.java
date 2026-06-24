package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, String> {

    List<PaymentJpaEntity> findByReservationId(String reservationId);
}
