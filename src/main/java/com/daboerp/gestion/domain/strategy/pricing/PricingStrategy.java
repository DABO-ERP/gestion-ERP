package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Reservation;
import java.math.BigDecimal;

/**
 * Strategy pattern for different pricing calculation rules.
 * Allows flexible pricing strategies without modifying existing code.
 */
public interface PricingStrategy {
    
    /**
     * Calculate the total price for a reservation.
     */
    BigDecimal calculatePrice(Reservation reservation);
    
    /**
     * Check if this strategy applies to the given reservation.
     */
    boolean isApplicable(Reservation reservation);
    
    /**
     * Priority for strategy selection (higher number = higher priority).
     */
    default int getPriority() {
        return 0;
    }
    
    /**
     * Name of the strategy for logging/debugging.
     */
    String getStrategyName();
}