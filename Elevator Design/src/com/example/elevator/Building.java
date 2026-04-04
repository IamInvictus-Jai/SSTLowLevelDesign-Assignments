package com.example.elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the building — holds floors and their external panels.
 */
public class Building {
    private final int totalFloors;
    private final List<ExternalPanel> panels = new ArrayList<>();

    public Building(int totalFloors, ElevatorController controller) {
        this.totalFloors = totalFloors;
        for (int i = 0; i < totalFloors; i++) {
            ExternalPanel panel = new ExternalPanel(i);
            panel.setController(controller);
            panels.add(panel);
        }
        System.out.println("[Building] " + totalFloors + " floors initialized.");
    }

    public ExternalPanel getPanelAt(int floor) {
        if (floor < 0 || floor >= totalFloors)
            throw new IllegalArgumentException("Invalid floor: " + floor);
        return panels.get(floor);
    }

    public int getTotalFloors() { return totalFloors; }
}
