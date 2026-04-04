package com.example.elevator;

import java.util.TreeSet;

/**
 * Represents a single elevator car.
 *
 * State machine:
 *   IDLE ──► MOVING_UP / MOVING_DOWN ──► IDLE (when queue empty)
 *   Any state ──► MAINTENANCE (via setMaintenance or emergency stop)
 *   MAINTENANCE ──► IDLE (via clearMaintenance)
 *
 * Weight limit: 700 KG. Cabin requests rejected if over limit.
 */
public class ElevatorCar {
    public static final double WEIGHT_LIMIT_KG = 700.0;

    private final String id;
    private int currentFloor;
    private ElevatorState state;
    private double currentWeightKg;
    private boolean doorOpen;

    // Sorted set of destination floors — allows efficient next-stop lookup
    private final TreeSet<Integer> destinations = new TreeSet<>();
    private final InternalPanel internalPanel;

    public ElevatorCar(String id, int initialFloor) {
        this.id = id;
        this.currentFloor = initialFloor;
        this.state = ElevatorState.IDLE;
        this.currentWeightKg = 0;
        this.doorOpen = false;
        this.internalPanel = new InternalPanel(this);
    }

    // -------------------------------------------------------------------------
    // Cabin request (from internal panel)
    // -------------------------------------------------------------------------
    public void addCabinRequest(CabinRequest req) {
        if (state == ElevatorState.MAINTENANCE) {
            System.out.println("[Car-" + id + "] Under maintenance. Request ignored.");
            return;
        }
        destinations.add(req.getDestinationFloor());
        System.out.println("[Car-" + id + "] Destination " + req.getDestinationFloor() + " queued. Queue: " + destinations);
        updateState();
    }

    // -------------------------------------------------------------------------
    // Floor request dispatched by controller
    // -------------------------------------------------------------------------
    public void assignFloorRequest(FloorRequest req) {
        if (state == ElevatorState.MAINTENANCE) {
            System.out.println("[Car-" + id + "] Under maintenance. Cannot accept request.");
            return;
        }
        destinations.add(req.getFloor());
        System.out.println("[Car-" + id + "] Assigned to floor " + req.getFloor() + ". Queue: " + destinations);
        updateState();
    }

    // -------------------------------------------------------------------------
    // Movement simulation — call step() to move one floor at a time
    // -------------------------------------------------------------------------
    public void step() {
        if (state == ElevatorState.MAINTENANCE || state == ElevatorState.IDLE) return;
        if (destinations.isEmpty()) { state = ElevatorState.IDLE; return; }

        if (state == ElevatorState.MOVING_UP) {
            currentFloor++;
        } else if (state == ElevatorState.MOVING_DOWN) {
            currentFloor--;
        }

        System.out.println("[Car-" + id + "] At floor " + currentFloor + " | state=" + state);

        if (destinations.contains(currentFloor)) {
            destinations.remove(currentFloor);
            state = ElevatorState.IDLE; // stop before opening door
            openDoor();
            closeDoor();
            System.out.println("[Car-" + id + "] Served floor " + currentFloor + ". Remaining: " + destinations);
        }

        updateState();
    }

    // -------------------------------------------------------------------------
    // Door control
    // -------------------------------------------------------------------------
    public void openDoor() {
        if (state == ElevatorState.MOVING_UP || state == ElevatorState.MOVING_DOWN) {
            System.out.println("[Car-" + id + "] Cannot open door while moving.");
            return;
        }
        doorOpen = true;
        System.out.println("[Car-" + id + "] Door OPEN at floor " + currentFloor);
    }

    public void closeDoor() {
        doorOpen = false;
        System.out.println("[Car-" + id + "] Door CLOSED at floor " + currentFloor);
    }

    // -------------------------------------------------------------------------
    // Emergency stop (alarm)
    // -------------------------------------------------------------------------
    public void triggerEmergencyStop() {
        System.out.println("[Car-" + id + "] EMERGENCY STOP at floor " + currentFloor + ". Going to MAINTENANCE.");
        destinations.clear();
        state = ElevatorState.MAINTENANCE;
        openDoor();
    }

    // -------------------------------------------------------------------------
    // Maintenance
    // -------------------------------------------------------------------------
    public void setMaintenance() {
        System.out.println("[Car-" + id + "] Entering MAINTENANCE mode.");
        destinations.clear();
        state = ElevatorState.MAINTENANCE;
    }

    public void clearMaintenance() {
        if (state != ElevatorState.MAINTENANCE) return;
        state = ElevatorState.IDLE;
        System.out.println("[Car-" + id + "] Maintenance cleared. Now IDLE at floor " + currentFloor);
    }

    // -------------------------------------------------------------------------
    // Weight
    // -------------------------------------------------------------------------
    public boolean canAcceptWeight(double kg) {
        return (currentWeightKg + kg) <= WEIGHT_LIMIT_KG;
    }

    public void addWeight(double kg) {
        if (!canAcceptWeight(kg)) {
            System.out.println("[Car-" + id + "] Weight limit exceeded! Max=" + WEIGHT_LIMIT_KG + "kg");
            return;
        }
        currentWeightKg += kg;
        System.out.println("[Car-" + id + "] Weight updated: " + currentWeightKg + "kg / " + WEIGHT_LIMIT_KG + "kg");
    }

    public void removeWeight(double kg) {
        currentWeightKg = Math.max(0, currentWeightKg - kg);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------
    private void updateState() {
        if (destinations.isEmpty()) {
            state = ElevatorState.IDLE;
            return;
        }
        // Pick direction toward nearest destination
        int next = nearestDestination();
        if (next > currentFloor)      state = ElevatorState.MOVING_UP;
        else if (next < currentFloor) state = ElevatorState.MOVING_DOWN;
        else                          state = ElevatorState.IDLE;
    }

    private int nearestDestination() {
        Integer above = destinations.ceiling(currentFloor);
        Integer below = destinations.floor(currentFloor);
        if (above == null) return below;
        if (below == null) return above;
        return (above - currentFloor) <= (currentFloor - below) ? above : below;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------
    public String getId()            { return id; }
    public int getCurrentFloor()     { return currentFloor; }
    public ElevatorState getState()  { return state; }
    public double getCurrentWeight() { return currentWeightKg; }
    public boolean isDoorOpen()      { return doorOpen; }
    public InternalPanel getInternalPanel() { return internalPanel; }
    public TreeSet<Integer> getDestinations() { return destinations; }

    @Override
    public String toString() {
        return "Car-" + id + "[floor=" + currentFloor + ", state=" + state
             + ", weight=" + currentWeightKg + "kg, queue=" + destinations + "]";
    }
}
