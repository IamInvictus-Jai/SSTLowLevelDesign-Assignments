package com.example.parking;

public class EntryGate {
    private final String id;
    private final int floor;    // gate is on this floor
    private final int position; // position index on that floor

    public EntryGate(String id, int floor, int position) {
        this.id = id;
        this.floor = floor;
        this.position = position;
    }

    public String getId() { return id; }
    public int getFloor() { return floor; }
    public int getPosition() { return position; }

    /**
     * Distance metric: floor difference counts more than position difference
     * so we prefer slots on the same floor first.
     */
    public int distanceTo(ParkingSlot slot) {
        return Math.abs(this.floor - slot.getFloor()) * 100
             + Math.abs(this.position - slot.getPosition());
    }
}
