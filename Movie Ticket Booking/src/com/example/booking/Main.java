package com.example.booking;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataStore store = new DataStore();
        PricingEngine pricingEngine = new PricingEngine();
        AdminService admin = new AdminService(store, pricingEngine);
        BookingService service = new BookingService(store, pricingEngine);

        System.out.println("=== Movie Ticket Booking System ===\n");

        // ---- Admin: setup ----
        Movie inception = admin.addMovie("Inception", 148, "Sci-Fi");
        Movie interstellar = admin.addMovie("Interstellar", 169, "Sci-Fi");

        Theatre pvr = admin.addTheatre("PVR Cinemas", "Bangalore");
        Theatre inox = admin.addTheatre("INOX", "Bangalore");

        // PVR: Hall 1 — 5 Bronze, 4 Silver, 3 Gold, 2 Diamond
        Hall pvrH1 = admin.addHall(pvr, "Hall 1", 5, 4, 3, 2, 150, 250, 400, 600);
        // INOX: Hall A — 4 Bronze, 4 Silver, 2 Gold, 2 Diamond
        Hall inoxHA = admin.addHall(inox, "Hall A", 4, 4, 2, 2, 120, 200, 350, 550);

        LocalDateTime morning  = LocalDateTime.of(2026, 4, 21, 10, 0);
        LocalDateTime evening  = LocalDateTime.of(2026, 4, 21, 19, 0);
        LocalDateTime saturday = LocalDateTime.of(2026, 4, 25, 20, 0); // Saturday evening

        Show show1 = admin.addShow(inception,    pvrH1,  pvr,  morning);
        Show show2 = admin.addShow(inception,    pvrH1,  pvr,  evening);
        Show show3 = admin.addShow(interstellar, inoxHA, inox, saturday);

        System.out.println();

        // ---- Admin: activate pricing rules ----
        admin.activatePricingRule(new TimeSlotPricingRule());
        admin.activatePricingRule(new DemandSurgePricingRule());
        System.out.println();

        // ---- Register users ----
        User alice = service.registerUser("Alice", "alice@sst.edu", "9876543210");
        User bob   = service.registerUser("Bob",   "bob@sst.edu",   "9876543211");

        // Duplicate email check
        try {
            service.registerUser("Alice2", "alice@sst.edu", "0000000000");
        } catch (IllegalArgumentException e) {
            System.out.println("[error] " + e.getMessage());
        }
        System.out.println();

        // ---- API: showTheatres(city) ----
        service.showTheatres("Bangalore");
        System.out.println();

        // ---- API: showMovies(city) ----
        service.showMovies("Bangalore");
        System.out.println();

        // ---- API: showShowsForMovie ----
        service.showShowsForMovie(inception.getId(), "Bangalore");
        System.out.println();

        // ---- API: showShowsForTheatre ----
        service.showShowsForTheatre(pvr.getId());
        System.out.println();

        // ---- API: getSeatMap (morning show — no surge, no time premium) ----
        System.out.println("--- Seat map: morning show (no premium) ---");
        List<ShowSeat> seatMap1 = service.getSeatMap(show1.getId());
        System.out.println();

        // ---- API: getSeatMap (evening show — time premium applies) ----
        System.out.println("--- Seat map: evening show (evening premium) ---");
        service.getSeatMap(show2.getId());
        System.out.println();

        // ---- API: bookTickets ----
        // Alice books 2 Bronze seats in morning show
        String seat1Id = seatMap1.get(0).getSeat().getId();
        String seat2Id = seatMap1.get(1).getSeat().getId();

        System.out.println("--- Alice books 2 Bronze seats (morning show) ---");
        Ticket t1 = service.bookTickets(show1.getId(), List.of(seat1Id, seat2Id),
                                         alice.getId(), PaymentMode.UPI);
        System.out.println();

        // Bob tries to book the same seat — should fail
        System.out.println("--- Bob tries to book same seat (should fail) ---");
        try {
            service.bookTickets(show1.getId(), List.of(seat1Id), bob.getId(), PaymentMode.CARD);
        } catch (IllegalStateException e) {
            System.out.println("[error] " + e.getMessage());
        }
        System.out.println();

        // Bob books a different seat
        String seat3Id = seatMap1.get(2).getSeat().getId();
        System.out.println("--- Bob books a different seat ---");
        Ticket t2 = service.bookTickets(show1.getId(), List.of(seat3Id),
                                         bob.getId(), PaymentMode.CARD);
        System.out.println();

        // ---- API: cancelBooking ----
        System.out.println("--- Alice cancels her booking ---");
        service.cancelBooking(t1.getBooking().getId());
        System.out.println();

        // ---- Saturday evening show — both time + demand surge (book many seats first) ----
        System.out.println("--- Saturday evening show (weekend + evening premium) ---");
        List<ShowSeat> satSeats = service.getSeatMap(show3.getId());
        System.out.println();

        // Book most seats to trigger demand surge
        System.out.println("--- Booking most seats to trigger demand surge ---");
        List<String> bulkIds = satSeats.subList(0, satSeats.size() - 2)
                .stream().map(ss -> ss.getSeat().getId()).collect(java.util.stream.Collectors.toList());
        service.bookTickets(show3.getId(), bulkIds, alice.getId(), PaymentMode.NET_BANKING);
        System.out.println();

        // Now check seat map — remaining seats should show surge price
        System.out.println("--- Seat map after high demand (surge should apply) ---");
        service.getSeatMap(show3.getId());
    }
}
