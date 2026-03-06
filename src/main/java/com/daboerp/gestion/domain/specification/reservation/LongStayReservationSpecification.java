package com.daboerp.gestion.domain.specification.reservation;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.specification.Specification;

/**
 * Specification for long stay reservations (more than specified number of nights).
 */
public class LongStayReservationSpecification implements Specification<Reservation> {
    
    private final long minimumNights;
    
    public LongStayReservationSpecification(long minimumNights) {
        if (minimumNights < 1) {
            throw new IllegalArgumentException("Minimum nights must be positive");
        }
        this.minimumNights = minimumNights;
    }
    
    @Override
    public boolean isSatisfiedBy(Reservation reservation) {
        return reservation.getNightCount() >= minimumNights;
    }
    
    /**
     * Factory method for standard long stay (7+ nights).
     */
    public static LongStayReservationSpecification standardLongStay() {
        return new LongStayReservationSpecification(7);
    }
}