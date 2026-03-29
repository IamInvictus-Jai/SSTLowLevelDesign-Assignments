package com.example.booking;

import java.time.LocalDateTime;

/**
 * Represents a specific seat for a specific show.
 * Holds runtime state: status, lock expiry, and computed price.
 *
 * All status mutations are synchronized to handle concurrent access.
 */
public class ShowSeat {
    private static final int LOCK_DURATION_MINUTES = 10;

    private final String showId;
    private final Seat seat;
    private SeatStatus status;
    private LocalDateTime lockExpiry;
    private double price; // computed by PricingEngine

    public ShowSeat(String showId, Seat seat, double price) {
        this.showId = showId;
        this.seat = seat;
        this.status = SeatStatus.AVAILABLE;
        this.price = price;
    }

    public String getShowId()  { return showId; }
    public Seat getSeat()      { return seat; }
    public double getPrice()   { return price; }
    public void setPrice(double price) { this.price = price; }

    public synchronized SeatStatus getStatus() {
        // Auto-expire stale locks
        if (status == SeatStatus.LOCKED && lockExpiry != null
                && LocalDateTime.now().isAfter(lockExpiry)) {
            status = SeatStatus.AVAILABLE;
            lockExpiry = null;
        }
        return status;
    }

    /**
     * Attempts to lock this seat. Returns true only if currently AVAILABLE.
     */
    public synchronized boolean lock() {
        if (getStatus() == SeatStatus.AVAILABLE) {
            status = SeatStatus.LOCKED;
            lockExpiry = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);
            return true;
        }
        return false;
    }

    /**
     * Confirms the seat as BOOKED. Only valid if currently LOCKED.
     */
    public synchronized boolean book() {
        if (status == SeatStatus.LOCKED) {
            status = SeatStatus.BOOKED;
            lockExpiry = null;
            return true;
        }
        return false;
    }

    /**
     * Releases the seat back to AVAILABLE (on cancellation or lock expiry).
     */
    public synchronized void release() {
        status = SeatStatus.AVAILABLE;
        lockExpiry = null;
    }

    @Override
    public String toString() {
        return seat + "[" + getStatus() + ", ₹" + String.format("%.0f", price) + "]";
    }
}
