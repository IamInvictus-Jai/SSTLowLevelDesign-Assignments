package com.example.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final String name;
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final Map<String, EntryGate> gates = new HashMap<>();
    private final SlotFinder slotFinder = new SlotFinder();

    public ParkingLot(String name) { this.name = name; }

    public void addFloor(ParkingFloor floor) { floors.add(floor); }
    public void addGate(EntryGate gate) { gates.put(gate.getId(), gate); }

    // -------------------------------------------------------------------------
    // API 1: park
    // -------------------------------------------------------------------------
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, String entryGateId) {
        EntryGate gate = gates.get(entryGateId);
        if (gate == null) throw new IllegalArgumentException("Unknown gate: " + entryGateId);

        ParkingSlot slot = slotFinder.findNearest(floors, gate, vehicle.getType(), requestedSlotType);
        if (slot == null) {
            System.out.println("[park] No available slot for " + vehicle + " from gate " + entryGateId);
            return null;
        }

        slot.occupy();
        ParkingTicket ticket = new ParkingTicket(vehicle, slot, entryTime);
        System.out.println("[park] " + vehicle + " parked at " + slot + " via gate " + entryGateId);
        System.out.println("       Ticket: " + ticket.getTicketId() + " | entry=" + entryTime);
        return ticket;
    }

    // -------------------------------------------------------------------------
    // API 2: status
    // -------------------------------------------------------------------------
    public void status() {
        Map<SlotType, int[]> counts = new HashMap<>();
        for (SlotType t : SlotType.values()) counts.put(t, new int[]{0, 0}); // [available, total]

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                int[] c = counts.get(slot.getType());
                c[1]++;
                if (!slot.isOccupied()) c[0]++;
            }
        }

        System.out.println("[status] " + name + " availability:");
        for (SlotType t : SlotType.values()) {
            int[] c = counts.get(t);
            System.out.println("  " + t + ": " + c[0] + "/" + c[1] + " available");
        }
    }

    // -------------------------------------------------------------------------
    // API 3: exit
    // -------------------------------------------------------------------------
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        if (ticket == null) throw new IllegalArgumentException("Invalid ticket");
        ticket.getSlot().vacate();
        Bill bill = new Bill(ticket, exitTime);
        System.out.println("[exit] " + bill);
        return bill;
    }
}
