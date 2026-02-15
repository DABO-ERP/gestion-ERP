package com.daboerp.gestion.domain.specification.reservation;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.specification.Specification;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Specification for reservations within a date range.
 */
public class DateRangeReservationSpecification implements Specification<Reservation> {
    
    private final LocalDate startDate;
    private final LocalDate endDate;
    
    public DateRangeReservationSpecification(LocalDate startDate, LocalDate endDate) {
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
    
    @Override
    public boolean isSatisfiedBy(Reservation reservation) {
        LocalDate checkIn = reservation.getCheckIn();
        LocalDate checkOut = reservation.getCheckOut();
        
        // Overlaps if: checkIn <= endDate AND checkOut >= startDate
        return !checkIn.isAfter(endDate) && !checkOut.isBefore(startDate);
    }
}