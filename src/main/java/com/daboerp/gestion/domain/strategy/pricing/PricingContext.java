package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Reservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Context class for pricing strategy pattern.
 * Selects and executes the appropriate pricing strategy.
 */
public class PricingContext {
    
    private final List<PricingStrategy> strategies;
    
    public PricingContext(List<PricingStrategy> strategies) {
        this.strategies = Objects.requireNonNull(strategies, "Strategies cannot be null");
        if (strategies.isEmpty()) {
            throw new IllegalArgumentException("At least one pricing strategy must be provided");
        }
    }
    
    /**
     * Calculate price using the most appropriate strategy.
     * Strategies are evaluated by priority (highest first) and applicability.
     */
    public BigDecimal calculatePrice(Reservation reservation) {
        Objects.requireNonNull(reservation, "Reservation cannot be null");
        
        PricingStrategy selectedStrategy = strategies.stream()
            .filter(strategy -> strategy.isApplicable(reservation))
            .max((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()))
            .orElseThrow(() -> new IllegalStateException("No applicable pricing strategy found"));
            
        return selectedStrategy.calculatePrice(reservation);
    }
    
    /**
     * Get the name of the strategy that would be used for the reservation.
     */
    public String getApplicableStrategyName(Reservation reservation) {
        Objects.requireNonNull(reservation, "Reservation cannot be null");
        
        return strategies.stream()
            .filter(strategy -> strategy.isApplicable(reservation))
            .max((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()))
            .map(PricingStrategy::getStrategyName)
            .orElse("No applicable strategy");
    }
}