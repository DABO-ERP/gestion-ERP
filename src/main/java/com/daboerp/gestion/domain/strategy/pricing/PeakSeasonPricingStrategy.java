package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Reservation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;

/**
 * Peak season pricing strategy - increased rates during high demand periods.
 */
public class PeakSeasonPricingStrategy implements PricingStrategy {
    
    private static final BigDecimal PEAK_MULTIPLIER = new BigDecimal("1.25"); // 25% increase
    
    @Override
    public BigDecimal calculatePrice(Reservation reservation) {
        BigDecimal standardPrice = new StandardPricingStrategy().calculatePrice(reservation);
        return standardPrice.multiply(PEAK_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public boolean isApplicable(Reservation reservation) {
        LocalDate checkIn = reservation.getCheckIn();
        LocalDate checkOut = reservation.getCheckOut();
        
        // Peak season: December, January, July, August
        return isPeakSeason(checkIn) || isPeakSeason(checkOut) || 
               hasOverlapWithPeakSeason(checkIn, checkOut);
    }
    
    @Override
    public int getPriority() {
        return 10; // Highest priority
    }
    
    @Override
    public String getStrategyName() {
        return "PeakSeasonPricing";
    }
    
    private boolean isPeakSeason(LocalDate date) {
        Month month = date.getMonth();
        return month == Month.DECEMBER || month == Month.JANUARY || 
               month == Month.JULY || month == Month.AUGUST;
    }
    
    private boolean hasOverlapWithPeakSeason(LocalDate checkIn, LocalDate checkOut) {
        LocalDate current = checkIn;
        while (!current.isAfter(checkOut)) {
            if (isPeakSeason(current)) {
                return true;
            }
            current = current.plusDays(1);
        }
        return false;
    }
}