package com.example.booking;

import java.time.LocalDateTime;
import java.util.UUID;

public class Show {
    private final String id;
    private final String movieId;
    private final String hallId;
    private final String theatreId;
    private final LocalDateTime startTime;

    public Show(String movieId, String hallId, String theatreId, LocalDateTime startTime) {
        this.id = UUID.randomUUID().toString();
        this.movieId = movieId;
        this.hallId = hallId;
        this.theatreId = theatreId;
        this.startTime = startTime;
    }

    public String getId()              { return id; }
    public String getMovieId()         { return movieId; }
    public String getHallId()          { return hallId; }
    public String getTheatreId()       { return theatreId; }
    public LocalDateTime getStartTime(){ return startTime; }

    @Override
    public String toString() { return "Show@" + startTime; }
}
