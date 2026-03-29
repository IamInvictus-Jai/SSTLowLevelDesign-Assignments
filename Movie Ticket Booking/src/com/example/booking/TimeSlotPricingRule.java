package com.example.booking;

import java.time.DayOfWeek;

/**
 * Increases price for premium time slots.
 * - Evening shows (18:00–23:00) → 15% premium
 * - Weekend shows → additional 10% premium
 */
public class TimeSlotPricingRule implements PricingRule {

    @Override
    public String getName() { return "TimeSlot"; }

    @Override
    public double apply(double currentPrice, ShowSeat showSeat, Show show, ShowSeatContext ctx) {
        double price = currentPrice;
        int hour = show.getStartTime().getHour();
        DayOfWeek day = show.getStartTime().getDayOfWeek();

        if (hour >= 18 && hour < 23) price *= 1.15;

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) price *= 1.10;

        return price;
    }
}
