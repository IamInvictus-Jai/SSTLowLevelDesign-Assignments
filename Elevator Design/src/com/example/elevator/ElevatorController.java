package com.example.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central controller — manages all elevator cars and dispatches floor requests
 * using the active DispatchStrategy.
 *
 * Strategy can be swapped at runtime via setStrategy().
 */
public class ElevatorController {
    private final List<ElevatorCar> cars = new ArrayList<>();
    private DispatchStrategy strategy;

    public ElevatorController(DispatchStrategy strategy) {
        this.strategy = strategy;
        System.out.println("[Controller] Initialized with strategy: " + strategy.getName());
    }

    public void addCar(ElevatorCar car) {
        cars.add(car);
        System.out.println("[Controller] Car-" + car.getId() + " registered.");
    }

    /** Swap dispatch strategy at runtime. */
    public void setStrategy(DispatchStrategy strategy) {
        this.strategy = strategy;
        System.out.println("[Controller] Strategy switched to: " + strategy.getName());
    }

    /** Called when a user presses UP or DOWN on an external floor panel. */
    public void handleFloorRequest(FloorRequest request) {
        System.out.println("[Controller] Handling " + request);
        ElevatorCar selected = strategy.selectCar(request, cars);
        if (selected == null) {
            System.out.println("[Controller] No available car for " + request);
            return;
        }
        System.out.println("[Controller] Dispatching Car-" + selected.getId() + " to floor " + request.getFloor());
        selected.assignFloorRequest(request);
    }

    /** Put a car into maintenance mode. */
    public void setMaintenance(String carId) {
        findCar(carId).ifPresent(ElevatorCar::setMaintenance);
    }

    /** Clear maintenance on a car. */
    public void clearMaintenance(String carId) {
        findCar(carId).ifPresent(ElevatorCar::clearMaintenance);
    }

    public List<ElevatorCar> getCars() { return Collections.unmodifiableList(cars); }

    public void printStatus() {
        System.out.println("[Controller] === Status ===");
        cars.forEach(c -> System.out.println("  " + c));
    }

    private java.util.Optional<ElevatorCar> findCar(String id) {
        return cars.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}
