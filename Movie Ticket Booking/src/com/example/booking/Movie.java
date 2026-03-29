package com.example.booking;

import java.util.UUID;

public class Movie {
    private final String id;
    private final String title;
    private final int durationMinutes;
    private final String genre;

    public Movie(String title, int durationMinutes, String genre) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
    }

    public String getId()               { return id; }
    public String getTitle()            { return title; }
    public int getDurationMinutes()     { return durationMinutes; }
    public String getGenre()            { return genre; }

    @Override
    public String toString() { return title + "[" + genre + ", " + durationMinutes + "min]"; }
}
