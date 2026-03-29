package com.example.booking;

/**
 * Strategy interface for pricing rules.
 * Each rule takes the current price and context, and returns an adjusted price.
 * Price must never go below the seat's base price — enforced by PricingEngine.
 */
public interface PricingRule {
    String getName();
    double apply(double currentPrice, ShowSeat showSeat, Show show, ShowSeatContext context);
}
