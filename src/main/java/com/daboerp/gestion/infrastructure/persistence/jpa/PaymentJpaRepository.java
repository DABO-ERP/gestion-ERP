package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Payment persistence.
 */
@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, String> {

    List<PaymentJpaEntity> findByReservationId(String reservationId);
}
