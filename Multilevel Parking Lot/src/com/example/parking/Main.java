package com.example.parking;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        // ---- Build the parking lot ----
        ParkingLot lot = new ParkingLot("SST Campus Parking");

        // Floor 0: 3 SMALL, 3 MEDIUM, 2 LARGE
        ParkingFloor f0 = new ParkingFloor(0);
        f0.addSlot(new ParkingSlot("F0-S1", SlotType.SMALL,  0, 1));
        f0.addSlot(new ParkingSlot("F0-S2", SlotType.SMALL,  0, 2));
        f0.addSlot(new ParkingSlot("F0-S3", SlotType.SMALL,  0, 3));
        f0.addSlot(new ParkingSlot("F0-M1", SlotType.MEDIUM, 0, 4));
        f0.addSlot(new ParkingSlot("F0-M2", SlotType.MEDIUM, 0, 5));
        f0.addSlot(new ParkingSlot("F0-M3", SlotType.MEDIUM, 0, 6));
        f0.addSlot(new ParkingSlot("F0-L1", SlotType.LARGE,  0, 7));
        f0.addSlot(new ParkingSlot("F0-L2", SlotType.LARGE,  0, 8));

        // Floor 1: 2 SMALL, 2 MEDIUM, 2 LARGE
        ParkingFloor f1 = new ParkingFloor(1);
        f1.addSlot(new ParkingSlot("F1-S1", SlotType.SMALL,  1, 1));
        f1.addSlot(new ParkingSlot("F1-S2", SlotType.SMALL,  1, 2));
        f1.addSlot(new ParkingSlot("F1-M1", SlotType.MEDIUM, 1, 3));
        f1.addSlot(new ParkingSlot("F1-M2", SlotType.MEDIUM, 1, 4));
        f1.addSlot(new ParkingSlot("F1-L1", SlotType.LARGE,  1, 5));
        f1.addSlot(new ParkingSlot("F1-L2", SlotType.LARGE,  1, 6));

        lot.addFloor(f0);
        lot.addFloor(f1);

        // Two entry gates on floor 0
        lot.addGate(new EntryGate("G1", 0, 1));
        lot.addGate(new EntryGate("G2", 0, 8));

        LocalDateTime t = LocalDateTime.of(2026, 4, 21, 9, 0);

        System.out.println("=== Multilevel Parking Lot Demo ===\n");

        // Initial status
        lot.status();
        System.out.println();

        // Park a bike — should get nearest SMALL from G1 → F0-S1
        Vehicle bike = new Vehicle("KA01AB1234", VehicleType.TWO_WHEELER);
        ParkingTicket t1 = lot.park(bike, t, SlotType.SMALL, "G1");
        System.out.println();

        // Park a car — should get nearest MEDIUM from G2 → F0-M3 (position 6, closest to G2 at pos 8)
        Vehicle car = new Vehicle("KA02CD5678", VehicleType.CAR);
        ParkingTicket t2 = lot.park(car, t, SlotType.MEDIUM, "G2");
        System.out.println();

        // Park a bus — LARGE only
        Vehicle bus = new Vehicle("KA03EF9999", VehicleType.BUS);
        ParkingTicket t3 = lot.park(bus, t, SlotType.LARGE, "G1");
        System.out.println();

        // Park a bike in MEDIUM slot (all SMALL full scenario — request MEDIUM directly)
        Vehicle bike2 = new Vehicle("KA04GH1111", VehicleType.TWO_WHEELER);
        ParkingTicket t4 = lot.park(bike2, t, SlotType.MEDIUM, "G1");
        System.out.println();

        // Status after parking
        lot.status();
        System.out.println();

        // Exit after 90 minutes
        LocalDateTime exitTime = t.plusMinutes(90);
        lot.exit(t1, exitTime); // bike in SMALL  → 2hrs × ₹20 = ₹40
        lot.exit(t2, exitTime); // car in MEDIUM  → 2hrs × ₹40 = ₹80
        lot.exit(t3, exitTime); // bus in LARGE   → 2hrs × ₹80 = ₹160
        lot.exit(t4, exitTime); // bike in MEDIUM → 2hrs × ₹40 = ₹80 (billed by slot type)
        System.out.println();

        // Status after exits
        lot.status();
    }
}
