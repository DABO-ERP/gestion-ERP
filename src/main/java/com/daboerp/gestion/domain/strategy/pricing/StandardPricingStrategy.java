package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Reservation;
import java.math.BigDecimal;

/**
 * Standard pricing strategy - base price per night.
 */
public class StandardPricingStrategy implements PricingStrategy {
    
    @Override
    public BigDecimal calculatePrice(Reservation reservation) {
        BigDecimal pricePerNight = reservation.getRoom().getRoomType().getBasePrice();
        long nightCount = reservation.getNightCount();
        return pricePerNight.multiply(BigDecimal.valueOf(nightCount));
    }
    
    @Override
    public boolean isApplicable(Reservation reservation) {
        // Standard strategy applies to all reservations as fallback
        return true;
    }
    
    @Override
    public int getPriority() {
        return 1; // Lowest priority - fallback strategy
    }
    
    @Override
    public String getStrategyName() {
        return "StandardPricing";
    }
}