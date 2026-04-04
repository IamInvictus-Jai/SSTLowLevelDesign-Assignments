package com.example.elevator;

/**
 * Panel on each floor (outside the elevator).
 * Buttons: UP and DOWN — routes request to the ElevatorController.
 */
public class ExternalPanel {
    private final int floor;
    private ElevatorController controller; // set after construction

    public ExternalPanel(int floor) {
        this.floor = floor;
    }

    public void setController(ElevatorController controller) {
        this.controller = controller;
    }

    public void pressUp() {
        System.out.println("[ExternalPanel-F" + floor + "] UP pressed.");
        controller.handleFloorRequest(new FloorRequest(floor, Direction.UP));
    }

    public void pressDown() {
        System.out.println("[ExternalPanel-F" + floor + "] DOWN pressed.");
        controller.handleFloorRequest(new FloorRequest(floor, Direction.DOWN));
    }
}
