package com.example.elevator;

import java.util.List;

/**
 * Strategy interface for selecting which elevator car should handle a floor request.
 * Implementations can be swapped at runtime on the ElevatorController.
 */
public interface DispatchStrategy {
    String getName();

    /**
     * Selects the best available car for the given request.
     * Cars in MAINTENANCE state must be excluded by the implementation.
     *
     * @return the selected ElevatorCar, or null if none available
     */
    ElevatorCar selectCar(FloorRequest request, List<ElevatorCar> cars);
}
