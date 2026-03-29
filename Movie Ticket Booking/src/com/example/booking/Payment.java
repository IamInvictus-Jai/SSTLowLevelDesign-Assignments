package com.example.booking;

import java.util.UUID;

public class Payment {
    private final String id;
    private final String bookingId;
    private final double amount;
    private final PaymentMode mode;
    private PaymentStatus status;

    public Payment(String bookingId, double amount, PaymentMode mode) {
        this.id = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.bookingId = bookingId;
        this.amount = amount;
        this.mode = mode;
        this.status = PaymentStatus.PENDING;
    }

    // Simulates payment processing — always succeeds in this demo
    public boolean process() {
        this.status = PaymentStatus.SUCCESS;
        return true;
    }

    public void refund() { this.status = PaymentStatus.REFUNDED; }

    public String getId()          { return id; }
    public String getBookingId()   { return bookingId; }
    public double getAmount()      { return amount; }
    public PaymentMode getMode()   { return mode; }
    public PaymentStatus getStatus() { return status; }

    @Override
    public String toString() {
        return id + "[" + mode + ", ₹" + String.format("%.2f", amount) + ", " + status + "]";
    }
}
