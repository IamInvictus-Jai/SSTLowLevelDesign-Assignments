package com.example.parking;

public class ParkingSlot {
    private final String id;
    private final SlotType type;
    private final int floor;
    private final int position; // used to compute distance from gate
    private boolean occupied;

    public ParkingSlot(String id, SlotType type, int floor, int position) {
        this.id = id;
        this.type = type;
        this.floor = floor;
        this.position = position;
        this.occupied = false;
    }

    public String getId() { return id; }
    public SlotType getType() { return type; }
    public int getFloor() { return floor; }
    public int getPosition() { return position; }
    public boolean isOccupied() { return occupied; }

    public void occupy() { this.occupied = true; }
    public void vacate() { this.occupied = false; }

    @Override
    public String toString() { return id + "[" + type + ",F" + floor + ",P" + position + "]"; }
}
