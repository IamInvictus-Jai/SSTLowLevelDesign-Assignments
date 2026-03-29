package com.example.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Booking {
    private final String id;
    private final String userId;
    private final String showId;
    private final List<ShowSeat> seats;
    private BookingStatus status;
    private Payment payment;

    public Booking(String userId, String showId, List<ShowSeat> seats) {
        this.id = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = userId;
        this.showId = showId;
        this.seats = new ArrayList<>(seats);
        this.status = BookingStatus.PENDING;
    }

    public double totalAmount() {
        return seats.stream().mapToDouble(ShowSeat::getPrice).sum();
    }

    public void confirm(Payment payment) {
        this.payment = payment;
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() { this.status = BookingStatus.CANCELLED; }

    public String getId()                    { return id; }
    public String getUserId()                { return userId; }
    public String getShowId()                { return showId; }
    public List<ShowSeat> getSeats()         { return Collections.unmodifiableList(seats); }
    public BookingStatus getStatus()         { return status; }
    public Payment getPayment()              { return payment; }

    @Override
    public String toString() {
        return id + "[show=" + showId + ", seats=" + seats.size() + ", " + status + "]";
    }
}
