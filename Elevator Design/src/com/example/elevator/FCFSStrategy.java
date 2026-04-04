package com.example.elevator;

import java.util.List;

/**
 * First Come First Serve — picks the first available (non-maintenance) car.
 */
public class FCFSStrategy implements DispatchStrategy {

    @Override
    public String getName() { return "FCFS"; }

    @Override
    public ElevatorCar selectCar(FloorRequest request, List<ElevatorCar> cars) {
        return cars.stream()
            .filter(c -> c.getState() != ElevatorState.MAINTENANCE)
            .findFirst()
            .orElse(null);
    }
}
