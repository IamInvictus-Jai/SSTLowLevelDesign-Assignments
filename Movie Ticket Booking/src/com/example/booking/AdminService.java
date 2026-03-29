package com.example.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin-only operations:
 * - addMovie, addTheatre, addShow
 * - manage pricing rules at runtime
 */
public class AdminService {
    private final DataStore store;
    private final PricingEngine pricingEngine;

    public AdminService(DataStore store, PricingEngine pricingEngine) {
        this.store = store;
        this.pricingEngine = pricingEngine;
    }

    public Movie addMovie(String title, int durationMinutes, String genre) {
        Movie movie = new Movie(title, durationMinutes, genre);
        store.saveMovie(movie);
        System.out.println("[admin] Movie added: " + movie);
        return movie;
    }

    public Theatre addTheatre(String name, String city) {
        Theatre theatre = new Theatre(name, city);
        store.saveTheatre(theatre);
        System.out.println("[admin] Theatre added: " + theatre);
        return theatre;
    }

    public Hall addHall(Theatre theatre, String hallName,
                        int smallSeats, int silverSeats, int goldSeats, int diamondSeats,
                        double bronzeBase, double silverBase, double goldBase, double diamondBase) {
        Hall hall = new Hall(hallName, theatre.getId());
        store.saveHall(hall);
        theatre.addHall(hall);

        int pos = 1;
        pos = addSeatsOfType(hall, "A", SeatType.BRONZE,  bronzeBase,  smallSeats,   pos);
        pos = addSeatsOfType(hall, "B", SeatType.SILVER,  silverBase,  silverSeats,  pos);
        pos = addSeatsOfType(hall, "C", SeatType.GOLD,    goldBase,    goldSeats,    pos);
              addSeatsOfType(hall, "D", SeatType.DIAMOND, diamondBase, diamondSeats, pos);

        System.out.println("[admin] Hall added: " + hallName + " in " + theatre.getName()
                + " (" + hall.getSeats().size() + " seats)");
        return hall;
    }

    private int addSeatsOfType(Hall hall, String row, SeatType type,
                                double basePrice, int count, int startPos) {
        for (int i = 0; i < count; i++) {
            hall.addSeat(new Seat(hall.getId(), row, startPos + i, type, basePrice));
        }
        return startPos + count;
    }

    public Show addShow(Movie movie, Hall hall, Theatre theatre, LocalDateTime startTime) {
        Show show = new Show(movie.getId(), hall.getId(), theatre.getId(), startTime);
        store.saveShow(show);

        // Create ShowSeat entries with initial prices = base prices
        List<ShowSeat> showSeatList = new ArrayList<>();
        for (Seat seat : hall.getSeats()) {
            showSeatList.add(new ShowSeat(show.getId(), seat, seat.getBasePrice()));
        }
        store.saveShowSeats(show.getId(), showSeatList);

        System.out.println("[admin] Show added: " + movie.getTitle()
                + " @ " + theatre.getName() + " | " + hall.getName()
                + " | " + startTime);
        return show;
    }

    // ---- Runtime pricing rule management ----
    public void activatePricingRule(PricingRule rule) {
        pricingEngine.addRule(rule);
    }

    public void deactivatePricingRule(String ruleName) {
        pricingEngine.removeRule(ruleName);
    }
}
