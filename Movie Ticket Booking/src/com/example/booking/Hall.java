package com.example.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Hall {
    private final String id;
    private final String name;
    private final String theatreId;
    private final List<Seat> seats = new ArrayList<>();

    public Hall(String name, String theatreId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.theatreId = theatreId;
    }

    public void addSeat(Seat seat) { seats.add(seat); }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public String getTheatreId() { return theatreId; }
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }

    @Override
    public String toString() { return name + "(Hall)"; }
}
