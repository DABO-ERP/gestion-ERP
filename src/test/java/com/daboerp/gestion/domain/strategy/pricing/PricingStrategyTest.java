package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Strategy Pattern implementation in pricing.
 */
class PricingStrategyTest {
    
    private Guest testGuest;
    private Room testRoom;
    private PricingContext pricingContext;
    
    @BeforeEach
    void setUp() {
        // Create test guest
        testGuest = Guest.create("John", "Doe", "john@example.com", "+123456789", 
                               LocalDate.of(1990, 1, 1), Nationality.UNITED_STATES, 
                               "PASSPORT123", DocumentType.PASSPORT);
        
        // Create test room with $100 base price
        RoomType roomType = RoomType.create("Standard", "Standard Room", 
                                          2, new BigDecimal("100.00"));
        testRoom = Room.create(101, roomType, 
                              List.of(Amenity.WIFI, Amenity.AIR_CONDITIONING));
        
        // Create pricing context with all strategies
        List<PricingStrategy> strategies = List.of(
            new PeakSeasonPricingStrategy(),
            new LongStayPricingStrategy(), 
            new StandardPricingStrategy()
        );
        pricingContext = new PricingContext(strategies);
    }
    
    @Test
    void shouldApplyStandardPricing() {
        // Given - 3 nights in off-peak season (March)
        LocalDate checkIn = LocalDate.of(2024, 3, 15);
        LocalDate checkOut = LocalDate.of(2024, 3, 18);
        
        Reservation reservation = Reservation.create(checkIn, checkOut, 
                                                   new BigDecimal("300.00"), 
                                                   Source.DIRECT, testGuest, testRoom);
        
        // When
        BigDecimal price = pricingContext.calculatePrice(reservation);
        String strategyName = pricingContext.getApplicableStrategyName(reservation);
        
        // Then
        assertEquals(new BigDecimal("300.00"), price); // $100 × 3 nights
        assertEquals("StandardPricing", strategyName);
    }
    
    @Test
    void shouldApplyLongStayDiscount() {
        // Given - 10 nights in off-peak season
        LocalDate checkIn = LocalDate.of(2024, 3, 15);
        LocalDate checkOut = LocalDate.of(2024, 3, 25);
        
        Reservation reservation = Reservation.create(checkIn, checkOut, 
                                                   new BigDecimal("1000.00"), 
                                                   Source.DIRECT, testGuest, testRoom);
        
        // When
        BigDecimal price = pricingContext.calculatePrice(reservation);
        String strategyName = pricingContext.getApplicableStrategyName(reservation);
        
        // Then
        assertEquals(new BigDecimal("900.00"), price); // $1000 - 10% discount = $900
        assertEquals("LongStayPricing", strategyName);
    }
    
    @Test
    void shouldApplyPeakSeasonPricing() {
        // Given - 3 nights in peak season (July)
        LocalDate checkIn = LocalDate.of(2024, 7, 15);
        LocalDate checkOut = LocalDate.of(2024, 7, 18);
        
        Reservation reservation = Reservation.create(checkIn, checkOut, 
                                                   new BigDecimal("300.00"), 
                                                   Source.DIRECT, testGuest, testRoom);
        
        // When
        BigDecimal price = pricingContext.calculatePrice(reservation);
        String strategyName = pricingContext.getApplicableStrategyName(reservation);
        
        // Then
        assertEquals(new BigDecimal("375.00"), price); // $300 × 1.25 = $375
        assertEquals("PeakSeasonPricing", strategyName);
    }
    
    @Test
    void shouldPrioritizePeakSeasonOverLongStay() {
        // Given - 10 nights in peak season (should use PeakSeason, not LongStay)
        LocalDate checkIn = LocalDate.of(2024, 7, 15);
        LocalDate checkOut = LocalDate.of(2024, 7, 25);
        
        Reservation reservation = Reservation.create(checkIn, checkOut, 
                                                   new BigDecimal("1000.00"), 
                                                   Source.DIRECT, testGuest, testRoom);
        
        // When
        String strategyName = pricingContext.getApplicableStrategyName(reservation);
        
        // Then
        assertEquals("PeakSeasonPricing", strategyName); // Higher priority wins
    }
}