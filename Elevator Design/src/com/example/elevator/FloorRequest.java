package com.example.elevator;

/**
 * Request made from an external floor panel (up/down button press).
 */
public class FloorRequest {
    private final int floor;
    private final Direction direction;

    public FloorRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor()           { return floor; }
    public Direction getDirection() { return direction; }

    @Override
    public String toString() {
        return "FloorRequest[floor=" + floor + ", dir=" + direction + "]";
    }
}
