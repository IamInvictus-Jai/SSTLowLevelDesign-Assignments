package com.example.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory store for all entities.
 * In a real system this would be a database layer.
 */
public class DataStore {
    private final Map<String, User>     users     = new HashMap<>();
    private final Map<String, Movie>    movies    = new HashMap<>();
    private final Map<String, Theatre>  theatres  = new HashMap<>();
    private final Map<String, Hall>     halls     = new HashMap<>();
    private final Map<String, Show>     shows     = new HashMap<>();
    private final Map<String, Booking>  bookings  = new HashMap<>();

    // showId → list of ShowSeats
    private final Map<String, List<ShowSeat>> showSeats = new HashMap<>();

    // ---- Users ----
    public void saveUser(User u)  { users.put(u.getId(), u); }
    public User getUserById(String id) { return users.get(id); }
    public boolean emailExists(String email) {
        return users.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    // ---- Movies ----
    public void saveMovie(Movie m)  { movies.put(m.getId(), m); }
    public Movie getMovieById(String id) { return movies.get(id); }
    public List<Movie> getAllMovies() { return new ArrayList<>(movies.values()); }

    // ---- Theatres ----
    public void saveTheatre(Theatre t) { theatres.put(t.getId(), t); }
    public Theatre getTheatreById(String id) { return theatres.get(id); }
    public List<Theatre> getTheatresByCity(String city) {
        List<Theatre> result = new ArrayList<>();
        for (Theatre t : theatres.values())
            if (t.getCity().equalsIgnoreCase(city)) result.add(t);
        return result;
    }

    // ---- Halls ----
    public void saveHall(Hall h) { halls.put(h.getId(), h); }
    public Hall getHallById(String id) { return halls.get(id); }

    // ---- Shows ----
    public void saveShow(Show s) { shows.put(s.getId(), s); }
    public Show getShowById(String id) { return shows.get(id); }
    public List<Show> getShowsByMovie(String movieId) {
        List<Show> result = new ArrayList<>();
        for (Show s : shows.values())
            if (s.getMovieId().equals(movieId)) result.add(s);
        return result;
    }
    public List<Show> getShowsByTheatre(String theatreId) {
        List<Show> result = new ArrayList<>();
        for (Show s : shows.values())
            if (s.getTheatreId().equals(theatreId)) result.add(s);
        return result;
    }
    public List<Show> getShowsByMovieAndCity(String movieId, String city) {
        List<Theatre> cityTheatres = getTheatresByCity(city);
        List<Show> result = new ArrayList<>();
        for (Show s : shows.values()) {
            if (!s.getMovieId().equals(movieId)) continue;
            if (cityTheatres.stream().anyMatch(t -> t.getId().equals(s.getTheatreId())))
                result.add(s);
        }
        return result;
    }

    // ---- ShowSeats ----
    public void saveShowSeats(String showId, List<ShowSeat> seats) {
        showSeats.put(showId, seats);
    }
    public List<ShowSeat> getShowSeats(String showId) {
        return showSeats.getOrDefault(showId, Collections.emptyList());
    }

    // ---- Bookings ----
    public void saveBooking(Booking b) { bookings.put(b.getId(), b); }
    public Booking getBookingById(String id) { return bookings.get(id); }
}
