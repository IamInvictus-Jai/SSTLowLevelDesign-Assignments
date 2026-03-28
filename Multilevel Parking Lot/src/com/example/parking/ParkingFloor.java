package com.example.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSlot> slots = new ArrayList<>();

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void addSlot(ParkingSlot slot) { slots.add(slot); }

    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSlot> getSlots() { return Collections.unmodifiableList(slots); }
}
