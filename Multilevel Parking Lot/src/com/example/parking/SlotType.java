package com.example.parking;

public enum SlotType {
    SMALL(20.0),
    MEDIUM(40.0),
    LARGE(80.0);

    public final double ratePerHour;

    SlotType(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }
}
