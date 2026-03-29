package com.example.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Applies all active pricing rules in order.
 * Final price is never allowed to drop below the seat's base price.
 * Admin can add/remove rules at runtime.
 */
public class PricingEngine {
    private final List<PricingRule> activeRules = new ArrayList<>();

    public void addRule(PricingRule rule) {
        activeRules.add(rule);
        System.out.println("[pricing] Rule activated: " + rule.getName());
    }

    public void removeRule(String ruleName) {
        activeRules.removeIf(r -> r.getName().equals(ruleName));
        System.out.println("[pricing] Rule deactivated: " + ruleName);
    }

    public List<PricingRule> getActiveRules() {
        return Collections.unmodifiableList(activeRules);
    }

    public double computePrice(ShowSeat showSeat, Show show, ShowSeatContext context) {
        double price = showSeat.getSeat().getBasePrice();
        for (PricingRule rule : activeRules) {
            price = rule.apply(price, showSeat, show, context);
        }
        // Never go below base price
        return Math.max(price, showSeat.getSeat().getBasePrice());
    }
}
