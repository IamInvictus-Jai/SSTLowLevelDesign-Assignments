package com.example.elevator;

/**
 * Panel inside the elevator car.
 * Buttons: floor numbers, open door, close door, alarm (emergency stop).
 */
public class InternalPanel {
    private final ElevatorCar car;

    public InternalPanel(ElevatorCar car) {
        this.car = car;
    }

    /** Passenger presses a destination floor button. */
    public void pressFloor(int floor) {
        System.out.println("[InternalPanel-" + car.getId() + "] Floor " + floor + " pressed.");
        car.addCabinRequest(new CabinRequest(floor));
    }

    /** Passenger presses open door button. */
    public void pressOpen() {
        System.out.println("[InternalPanel-" + car.getId() + "] Open door pressed.");
        car.openDoor();
    }

    /** Passenger presses close door button. */
    public void pressClose() {
        System.out.println("[InternalPanel-" + car.getId() + "] Close door pressed.");
        car.closeDoor();
    }

    /** Passenger presses alarm — triggers emergency stop. */
    public void pressAlarm() {
        System.out.println("[InternalPanel-" + car.getId() + "] ALARM pressed! Emergency stop.");
        car.triggerEmergencyStop();
    }
}
