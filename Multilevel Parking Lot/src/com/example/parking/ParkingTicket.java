package com.example.parking;

import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final LocalDateTime entryTime;

    public ParkingTicket(Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime) {
        this.ticketId = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSlot getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }

    @Override
    public String toString() {
        return "Ticket{" + ticketId + " | vehicle=" + vehicle
             + " | slot=" + slot + " | entry=" + entryTime + "}";
    }
}
