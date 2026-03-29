package com.example.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Theatre {
    private final String id;
    private final String name;
    private final String city;
    private final List<Hall> halls = new ArrayList<>();

    public Theatre(String name, String city) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.city = city;
    }

    public void addHall(Hall hall) { halls.add(hall); }

    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getCity()        { return city; }
    public List<Hall> getHalls()   { return Collections.unmodifiableList(halls); }

    @Override
    public String toString() { return name + ", " + city; }
}
