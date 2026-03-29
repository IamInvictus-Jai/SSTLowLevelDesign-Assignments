package com.example.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer-facing operations.
 */
public class BookingService {
    private final DataStore store;
    private final PricingEngine pricingEngine;

    public BookingService(DataStore store, PricingEngine pricingEngine) {
        this.store = store;
        this.pricingEngine = pricingEngine;
    }

    // -------------------------------------------------------------------------
    // User registration
    // -------------------------------------------------------------------------
    public User registerUser(String name, String email, String phone) {
        if (store.emailExists(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        User user = new User(name, email, phone);
        store.saveUser(user);
        System.out.println("[user] Registered: " + user);
        return user;
    }

    // -------------------------------------------------------------------------
    // API: showTheatres(city)
    // -------------------------------------------------------------------------
    public List<Theatre> showTheatres(String city) {
        List<Theatre> result = store.getTheatresByCity(city);
        System.out.println("[query] Theatres in " + city + ":");
        result.forEach(t -> System.out.println("  - " + t.getName()));
        return result;
    }

    // -------------------------------------------------------------------------
    // API: showMovies(city)
    // -------------------------------------------------------------------------
    public List<Movie> showMovies(String city) {
        List<Theatre> cityTheatres = store.getTheatresByCity(city);
        List<Movie> result = store.getAllMovies().stream()
            .filter(m -> store.getShowsByMovieAndCity(m.getId(), city).size() > 0)
            .collect(Collectors.toList());
        System.out.println("[query] Movies in " + city + ":");
        result.forEach(m -> System.out.println("  - " + m.getTitle()));
        return result;
    }

    // -------------------------------------------------------------------------
    // API: showShowsForMovie(movieId, city) — all theatres + slots for a movie
    // -------------------------------------------------------------------------
    public List<Show> showShowsForMovie(String movieId, String city) {
        List<Show> shows = store.getShowsByMovieAndCity(movieId, city);
        Movie movie = store.getMovieById(movieId);
        System.out.println("[query] Shows for " + movie.getTitle() + " in " + city + ":");
        for (Show s : shows) {
            Theatre t = store.getTheatreById(s.getTheatreId());
            Hall h = store.getHallById(s.getHallId());
            System.out.println("  - " + t.getName() + " | " + h.getName() + " | " + s.getStartTime() + " [showId=" + s.getId() + "]");
        }
        return shows;
    }

    // -------------------------------------------------------------------------
    // API: showShowsForTheatre(theatreId) — all movies + slots in a theatre
    // -------------------------------------------------------------------------
    public List<Show> showShowsForTheatre(String theatreId) {
        List<Show> shows = store.getShowsByTheatre(theatreId);
        Theatre t = store.getTheatreById(theatreId);
        System.out.println("[query] Shows at " + t.getName() + ":");
        for (Show s : shows) {
            Movie m = store.getMovieById(s.getMovieId());
            Hall h = store.getHallById(s.getHallId());
            System.out.println("  - " + m.getTitle() + " | " + h.getName() + " | " + s.getStartTime() + " [showId=" + s.getId() + "]");
        }
        return shows;
    }

    // -------------------------------------------------------------------------
    // API: getSeatMap(showId) — display seat availability with prices
    // -------------------------------------------------------------------------
    public List<ShowSeat> getSeatMap(String showId) {
        List<ShowSeat> seats = store.getShowSeats(showId);
        Show show = store.getShowById(showId);
        ShowSeatContext ctx = buildContext(seats);

        System.out.println("[seatmap] Show: " + showId);
        // Refresh prices based on current demand
        for (ShowSeat ss : seats) {
            double price = pricingEngine.computePrice(ss, show, ctx);
            ss.setPrice(price);
        }
        seats.forEach(ss -> System.out.println("  " + ss));
        return seats;
    }

    // -------------------------------------------------------------------------
    // API: bookTickets(showId, seatIds, userId, paymentMode)
    // -------------------------------------------------------------------------
    public Ticket bookTickets(String showId, List<String> seatIds,
                               String userId, PaymentMode paymentMode) {
        List<ShowSeat> showSeatList = store.getShowSeats(showId);
        Show show = store.getShowById(showId);
        ShowSeatContext ctx = buildContext(showSeatList);

        // Find requested ShowSeats
        List<ShowSeat> requested = showSeatList.stream()
            .filter(ss -> seatIds.contains(ss.getSeat().getId()))
            .collect(Collectors.toList());

        if (requested.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more seat IDs not found for this show.");
        }

        // Refresh prices before locking
        for (ShowSeat ss : requested) {
            ss.setPrice(pricingEngine.computePrice(ss, show, ctx));
        }

        // Lock all requested seats atomically
        List<ShowSeat> locked = new ArrayList<>();
        for (ShowSeat ss : requested) {
            if (!ss.lock()) {
                // Rollback already-locked seats
                locked.forEach(ShowSeat::release);
                throw new IllegalStateException("Seat " + ss.getSeat() + " is no longer available.");
            }
            locked.add(ss);
        }

        // Create booking
        Booking booking = new Booking(userId, showId, locked);
        store.saveBooking(booking);
        System.out.println("[booking] Created: " + booking + " | total=Rs." + String.format("%.2f", booking.totalAmount()));

        // Process payment
        Payment payment = new Payment(booking.getId(), booking.totalAmount(), paymentMode);
        if (!payment.process()) {
            locked.forEach(ShowSeat::release);
            booking.cancel();
            throw new IllegalStateException("Payment failed.");
        }

        // Confirm booking and mark seats as BOOKED
        booking.confirm(payment);
        locked.forEach(ShowSeat::book);

        System.out.println("[payment] " + payment);

        // Generate ticket
        Movie movie = store.getMovieById(show.getMovieId());
        Theatre theatre = store.getTheatreById(show.getTheatreId());
        Ticket ticket = new Ticket(booking, show, movie, theatre);
        ticket.print();
        return ticket;
    }

    // -------------------------------------------------------------------------
    // API: cancelBooking(bookingId)
    // -------------------------------------------------------------------------
    public void cancelBooking(String bookingId) {
        Booking booking = store.getBookingById(bookingId);
        if (booking == null) throw new IllegalArgumentException("Booking not found: " + bookingId);
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled.");
        }

        // Release seats
        booking.getSeats().forEach(ShowSeat::release);

        // Refund payment
        booking.getPayment().refund();
        booking.cancel();

        System.out.println("[cancel] Booking " + bookingId + " cancelled. Refund issued via "
                + booking.getPayment().getMode() + " for Rs." + String.format("%.2f", booking.totalAmount()));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------
    private ShowSeatContext buildContext(List<ShowSeat> seats) {
        int total = seats.size();
        int booked = (int) seats.stream().filter(s -> s.getStatus() == SeatStatus.BOOKED).count();
        int locked = (int) seats.stream().filter(s -> s.getStatus() == SeatStatus.LOCKED).count();
        return new ShowSeatContext(total, booked, locked);
    }
}
