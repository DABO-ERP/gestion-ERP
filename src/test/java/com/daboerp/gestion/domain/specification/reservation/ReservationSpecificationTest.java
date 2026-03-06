package com.daboerp.gestion.domain.specification.reservation;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.specification.Specification;
import com.daboerp.gestion.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Specification Pattern implementation.
 */
class ReservationSpecificationTest {
    
    private Reservation activeReservation;
    private Reservation shortStayReservation;
    private Reservation longStayReservation;
    
    @BeforeEach
    void setUp() {
        Guest guest = Guest.create("John", "Doe", "john@example.com", "+123456789", 
                                 LocalDate.of(1990, 1, 1), Nationality.UNITED_STATES, 
                                 "PASSPORT123", DocumentType.PASSPORT);
        
        RoomType roomType = RoomType.create("Standard", "Standard Room", 
                                          2, new BigDecimal("100.00"));
        Room room = Room.create(101, roomType, List.of(Amenity.WIFI));
        
        // Active reservation (3 nights)
        activeReservation = Reservation.create(
            LocalDate.of(2024, 6, 15),
            LocalDate.of(2024, 6, 18),
            new BigDecimal("300.00"),
            Source.DIRECT, guest, room
        );
        
        // Short stay (5 nights)
        shortStayReservation = Reservation.create(
            LocalDate.of(2024, 6, 15),
            LocalDate.of(2024, 6, 20),
            new BigDecimal("500.00"),
            Source.DIRECT, guest, room
        );
        
        // Long stay (10 nights)
        longStayReservation = Reservation.create(
            LocalDate.of(2024, 6, 15),
            LocalDate.of(2024, 6, 25),
            new BigDecimal("1000.00"),
            Source.DIRECT, guest, room
        );
    }
    
    @Test
    void shouldIdentifyActiveReservations() {
        // Given
        Specification<Reservation> activeSpec = new ActiveReservationSpecification();
        
        // When & Then
        assertTrue(activeSpec.isSatisfiedBy(activeReservation));
        assertTrue(activeSpec.isSatisfiedBy(shortStayReservation));
        assertTrue(activeSpec.isSatisfiedBy(longStayReservation));
    }
    
    @Test
    void shouldIdentifyLongStayReservations() {
        // Given
        Specification<Reservation> longStaySpec = LongStayReservationSpecification.standardLongStay();
        
        // When & Then
        assertFalse(longStaySpec.isSatisfiedBy(activeReservation)); // 3 nights
        assertFalse(longStaySpec.isSatisfiedBy(shortStayReservation)); // 5 nights
        assertTrue(longStaySpec.isSatisfiedBy(longStayReservation)); // 10 nights
    }
    
    @Test
    void shouldFilterByDateRange() {
        // Given
        LocalDate rangeStart = LocalDate.of(2024, 6, 16);
        LocalDate rangeEnd = LocalDate.of(2024, 6, 17);
        Specification<Reservation> dateRangeSpec = new DateRangeReservationSpecification(rangeStart, rangeEnd);
        
        // When & Then - all reservations overlap with the range
        assertTrue(dateRangeSpec.isSatisfiedBy(activeReservation));
        assertTrue(dateRangeSpec.isSatisfiedBy(shortStayReservation));
        assertTrue(dateRangeSpec.isSatisfiedBy(longStayReservation));
    }
    
    @Test
    void shouldCombineSpecificationsWithAnd() {
        // Given
        Specification<Reservation> activeSpec = new ActiveReservationSpecification();
        Specification<Reservation> longStaySpec = LongStayReservationSpecification.standardLongStay();
        
        Specification<Reservation> activeLongStay = activeSpec.and(longStaySpec);
        
        // When & Then
        assertFalse(activeLongStay.isSatisfiedBy(activeReservation)); // Active but not long stay
        assertFalse(activeLongStay.isSatisfiedBy(shortStayReservation)); // Active but not long stay
        assertTrue(activeLongStay.isSatisfiedBy(longStayReservation)); // Active AND long stay
    }
    
    @Test
    void shouldCombineSpecificationsWithOr() {
        // Given
        Specification<Reservation> shortRange = new DateRangeReservationSpecification(
            LocalDate.of(2024, 6, 14), LocalDate.of(2024, 6, 16)
        );
        Specification<Reservation> longStaySpec = LongStayReservationSpecification.standardLongStay();
        
        Specification<Reservation> shortRangeOrLongStay = shortRange.or(longStaySpec);
        
        // When & Then
        assertTrue(shortRangeOrLongStay.isSatisfiedBy(activeReservation)); // Matches date range
        assertTrue(shortRangeOrLongStay.isSatisfiedBy(longStayReservation)); // Matches long stay
    }
    
    @Test
    void shouldNegateSpecification() {
        // Given
        Specification<Reservation> longStaySpec = LongStayReservationSpecification.standardLongStay();
        Specification<Reservation> notLongStay = longStaySpec.not();
        
        // When & Then
        assertTrue(notLongStay.isSatisfiedBy(activeReservation)); // NOT long stay
        assertTrue(notLongStay.isSatisfiedBy(shortStayReservation)); // NOT long stay
        assertFalse(notLongStay.isSatisfiedBy(longStayReservation)); // IS long stay
    }
}