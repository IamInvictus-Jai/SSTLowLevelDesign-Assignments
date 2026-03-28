package com.example.parking;

import java.time.Duration;
import java.time.LocalDateTime;

public class Bill {
    private final ParkingTicket ticket;
    private final LocalDateTime exitTime;
    private final double amount;

    public Bill(ParkingTicket ticket, LocalDateTime exitTime) {
        this.ticket = ticket;
        this.exitTime = exitTime;
        this.amount = calculateAmount();
    }

    private double calculateAmount() {
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        // Minimum 1 hour, then ceil to next hour
        double hours = Math.max(1.0, Math.ceil(minutes / 60.0));
        return hours * ticket.getSlot().getType().ratePerHour;
    }

    public ParkingTicket getTicket() { return ticket; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        return "Bill{ticket=" + ticket.getTicketId()
             + " | vehicle=" + ticket.getVehicle()
             + " | slot=" + ticket.getSlot()
             + " | duration=" + minutes + "min"
             + " | amount=₹" + String.format("%.2f", amount) + "}";
    }
}
