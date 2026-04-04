package com.example.elevator;

import java.util.Comparator;
import java.util.List;

/**
 * Shortest Path First — picks the available car closest to the requested floor.
 * Among ties, prefers IDLE cars, then cars already moving in the same direction.
 */
public class ShortestPathStrategy implements DispatchStrategy {

    @Override
    public String getName() { return "ShortestPath"; }

    @Override
    public ElevatorCar selectCar(FloorRequest request, List<ElevatorCar> cars) {
        return cars.stream()
            .filter(c -> c.getState() != ElevatorState.MAINTENANCE)
            .min(Comparator
                .comparingInt((ElevatorCar c) -> Math.abs(c.getCurrentFloor() - request.getFloor()))
                .thenComparingInt(c -> statePriority(c, request.getDirection())))
            .orElse(null);
    }

    /**
     * Lower score = higher priority.
     * IDLE = 0, moving in same direction = 1, moving in opposite direction = 2
     */
    private int statePriority(ElevatorCar car, Direction requestedDir) {
        if (car.getState() == ElevatorState.IDLE) return 0;
        if (car.getState() == ElevatorState.MOVING_UP   && requestedDir == Direction.UP)   return 1;
        if (car.getState() == ElevatorState.MOVING_DOWN && requestedDir == Direction.DOWN) return 1;
        return 2;
    }
}
