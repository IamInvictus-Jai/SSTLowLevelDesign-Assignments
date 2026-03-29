package com.example.booking;

/**
 * Increases price when occupancy is high.
 * - Above 70% occupancy → 25% surge
 * - Above 90% occupancy → 50% surge
 */
public class DemandSurgePricingRule implements PricingRule {

    @Override
    public String getName() { return "DemandSurge"; }

    @Override
    public double apply(double currentPrice, ShowSeat showSeat, Show show, ShowSeatContext ctx) {
        double ratio = ctx.occupancyRatio();
        if (ratio >= 0.90) return currentPrice * 1.50;
        if (ratio >= 0.70) return currentPrice * 1.25;
        return currentPrice;
    }
}
