package com.example.booking;

import java.util.UUID;

public class Ticket {
    private final String id;
    private final Booking booking;
    private final Show show;
    private final Movie movie;
    private final Theatre theatre;

    public Ticket(Booking booking, Show show, Movie movie, Theatre theatre) {
        this.id = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.booking = booking;
        this.show = show;
        this.movie = movie;
        this.theatre = theatre;
    }

    public String getId()       { return id; }
    public Booking getBooking() { return booking; }

    public void print() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("  TICKET: " + id);
        System.out.println("  Movie  : " + movie.getTitle());
        System.out.println("  Theatre: " + theatre.getName() + ", " + theatre.getCity());
        System.out.println("  Show   : " + show.getStartTime());
        System.out.println("  Seats  : " + booking.getSeats().stream()
                .map(ss -> ss.getSeat().toString()).reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b));
        System.out.println("  Amount : Rs." + String.format("%.2f", booking.totalAmount()));
        System.out.println("  Payment: " + booking.getPayment());
        System.out.println("  Booking: " + booking.getId());
        System.out.println("╚══════════════════════════════════════╝");
    }
}
