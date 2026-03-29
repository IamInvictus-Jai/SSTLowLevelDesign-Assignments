package com.example.booking;

/**
 * Contextual data passed to pricing rules so they can make decisions
 * without needing direct access to the full seat map.
 */
public class ShowSeatContext {
    public final int totalSeats;
    public final int bookedSeats;
    public final int lockedSeats;

    public ShowSeatContext(int totalSeats, int bookedSeats, int lockedSeats) {
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
        this.lockedSeats = lockedSeats;
    }

    public int unavailableSeats() { return bookedSeats + lockedSeats; }
    public double occupancyRatio() { return (double) unavailableSeats() / totalSeats; }
}
