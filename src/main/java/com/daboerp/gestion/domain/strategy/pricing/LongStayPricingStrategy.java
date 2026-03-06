package com.daboerp.gestion.domain.strategy.pricing;

import com.daboerp.gestion.domain.entity.Reservation;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Long stay pricing strategy - discount for stays longer than 7 nights.
 */
public class LongStayPricingStrategy implements PricingStrategy {
    
    private static final long LONG_STAY_THRESHOLD = 7;
    private static final BigDecimal DISCOUNT_PERCENTAGE = new BigDecimal("0.10"); // 10% discount
    
    @Override
    public BigDecimal calculatePrice(Reservation reservation) {
        BigDecimal standardPrice = new StandardPricingStrategy().calculatePrice(reservation);
        BigDecimal discount = standardPrice.multiply(DISCOUNT_PERCENTAGE);
        return standardPrice.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public boolean isApplicable(Reservation reservation) {
        return reservation.getNightCount() >= LONG_STAY_THRESHOLD;
    }
    
    @Override
    public int getPriority() {
        return 5; // Higher priority than standard
    }
    
    @Override
    public String getStrategyName() {
        return "LongStayPricing";
    }
}