package com.example.booking;

import java.util.UUID;

public class Seat {
    private final String id;
    private final String hallId;
    private final String row;
    private final int number;
    private final SeatType type;
    private final double basePrice; // base price for this seat type in this theatre

    public Seat(String hallId, String row, int number, SeatType type, double basePrice) {
        this.id = UUID.randomUUID().toString();
        this.hallId = hallId;
        this.row = row;
        this.number = number;
        this.type = type;
        this.basePrice = basePrice;
    }

    public String getId()        { return id; }
    public String getHallId()    { return hallId; }
    public String getRow()       { return row; }
    public int getNumber()       { return number; }
    public SeatType getType()    { return type; }
    public double getBasePrice() { return basePrice; }

    @Override
    public String toString() { return row + number + "(" + type + ")"; }
}
