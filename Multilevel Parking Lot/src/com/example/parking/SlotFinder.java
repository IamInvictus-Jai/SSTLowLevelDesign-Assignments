package com.example.parking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Finds the nearest available compatible slot from a given entry gate.
 *
 * Compatibility:
 *   TWO_WHEELER → SMALL, MEDIUM, LARGE (prefers smallest)
 *   CAR         → MEDIUM, LARGE        (prefers smallest)
 *   BUS         → LARGE only
 *
 * If a preferred slot type is requested, try that first.
 * Fall back to larger compatible types if unavailable.
 */
public class SlotFinder {

    public ParkingSlot findNearest(List<ParkingFloor> floors, EntryGate gate,
                                   VehicleType vehicleType, SlotType requestedType) {
        List<SlotType> preference = buildPreference(vehicleType, requestedType);

        for (SlotType slotType : preference) {
            ParkingSlot best = floors.stream()
                .flatMap(f -> f.getSlots().stream())
                .filter(s -> !s.isOccupied() && s.getType() == slotType)
                .min(Comparator.comparingInt(gate::distanceTo))
                .orElse(null);
            if (best != null) return best;
        }
        return null; // no slot available
    }

    /**
     * Builds the ordered list of slot types to try, starting from requestedType
     * and escalating to larger types if needed, within vehicle compatibility.
     */
    private List<SlotType> buildPreference(VehicleType vehicleType, SlotType requestedType) {
        List<SlotType> compatible = compatibleTypes(vehicleType);
        List<SlotType> preference = new ArrayList<>();

        // Start from requestedType (or smallest compatible if null), go up
        boolean found = requestedType == null;
        for (SlotType t : compatible) {
            if (!found && t == requestedType) found = true;
            if (found) preference.add(t);
        }
        return preference;
    }

    private List<SlotType> compatibleTypes(VehicleType vehicleType) {
        List<SlotType> types = new ArrayList<>();
        switch (vehicleType) {
            case TWO_WHEELER: types.add(SlotType.SMALL);  // fall through
            case CAR:         types.add(SlotType.MEDIUM); // fall through
            case BUS:         types.add(SlotType.LARGE);  break;
        }
        return types;
    }
}
