package com.example.elevator;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Elevator System Demo ===\n");

        // ---- Setup ----
        ElevatorController controller = new ElevatorController(new ShortestPathStrategy());

        ElevatorCar carA = new ElevatorCar("A", 0); // starts at floor 0
        ElevatorCar carB = new ElevatorCar("B", 5); // starts at floor 5
        ElevatorCar carC = new ElevatorCar("C", 2); // starts at floor 2

        controller.addCar(carA);
        controller.addCar(carB);
        controller.addCar(carC);

        Building building = new Building(10, controller);
        System.out.println();

        // ---- Initial status ----
        controller.printStatus();
        System.out.println();

        // ---- Scenario 1: User on floor 3 presses UP ----
        // ShortestPath: carC is at floor 2 (distance 1), carA at 0 (distance 3), carB at 5 (distance 2)
        // → carC should be selected
        System.out.println("--- Scenario 1: User on floor 3 presses UP ---");
        building.getPanelAt(3).pressUp();
        System.out.println();

        // ---- Scenario 2: User on floor 7 presses DOWN ----
        // carB at floor 5 (distance 2) is closest
        System.out.println("--- Scenario 2: User on floor 7 presses DOWN ---");
        building.getPanelAt(7).pressDown();
        System.out.println();

        // ---- Simulate movement: step cars ----
        System.out.println("--- Stepping cars toward destinations ---");
        for (int i = 0; i < 3; i++) {
            carC.step(); // carC moving from 2 → 3
            carB.step(); // carB moving from 5 → 7
        }
        System.out.println();

        // ---- Scenario 3: Passenger inside carC presses floor 8 ----
        System.out.println("--- Scenario 3: Passenger in Car-C presses floor 8 ---");
        carC.getInternalPanel().pressFloor(8);
        System.out.println();

        // Step carC to floor 8
        System.out.println("--- Stepping Car-C to floor 8 ---");
        for (int i = 0; i < 5; i++) carC.step();
        System.out.println();

        // ---- Scenario 4: Weight limit check ----
        System.out.println("--- Scenario 4: Weight limit check on Car-A ---");
        carA.addWeight(300);
        carA.addWeight(300);
        carA.addWeight(200); // should warn: 800 > 700
        System.out.println();

        // ---- Scenario 5: Car-B goes under maintenance ----
        System.out.println("--- Scenario 5: Car-B goes under maintenance ---");
        controller.setMaintenance("B");
        System.out.println();

        // User on floor 6 presses UP — carB should be skipped
        System.out.println("--- User on floor 6 presses UP (Car-B in maintenance) ---");
        building.getPanelAt(6).pressUp();
        System.out.println();

        // ---- Scenario 6: Emergency stop (alarm) in Car-C ----
        System.out.println("--- Scenario 6: Passenger triggers alarm in Car-C ---");
        carC.getInternalPanel().pressFloor(5); // queue a destination first
        carC.getInternalPanel().pressAlarm();  // emergency stop clears queue
        System.out.println();

        // ---- Scenario 7: Switch strategy to FCFS ----
        System.out.println("--- Scenario 7: Admin switches strategy to FCFS ---");
        controller.setStrategy(new FCFSStrategy());
        building.getPanelAt(1).pressDown();
        System.out.println();

        // ---- Scenario 8: Clear maintenance on Car-B ----
        System.out.println("--- Scenario 8: Maintenance cleared on Car-B ---");
        controller.clearMaintenance("B");
        System.out.println();

        // ---- Final status ----
        System.out.println("--- Final Status ---");
        controller.printStatus();
    }
}
