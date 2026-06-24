package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.valueobject.PaymentId;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Payment domain entity and JPA entity.
 */
@Component
public class PaymentMapper {

    public PaymentJpaEntity toJpaEntity(Payment payment) {
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setId(payment.getId().getValue());
        entity.setReservationId(payment.getReservationId().getValue());
        entity.setAmount(payment.getAmount());
        entity.setMethod(payment.getMethod());
        entity.setPaidAt(payment.getPaidAt());
        entity.setNote(payment.getNote());
        entity.setCreatedAt(payment.getCreatedAt());
        return entity;
    }

    public Payment toDomainEntity(PaymentJpaEntity entity) {
        return Payment.reconstitute(
            PaymentId.of(entity.getId()),
            ReservationId.of(entity.getReservationId()),
            entity.getAmount(),
            entity.getMethod(),
            entity.getPaidAt(),
            entity.getNote(),
            entity.getCreatedAt()
        );
    }
}
