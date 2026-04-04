package com.example.elevator;

/**
 * Request made from inside the elevator car (destination floor button press).
 */
public class CabinRequest {
    private final int destinationFloor;

    public CabinRequest(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public int getDestinationFloor() { return destinationFloor; }

    @Override
    public String toString() {
        return "CabinRequest[dest=" + destinationFloor + "]";
    }
}
