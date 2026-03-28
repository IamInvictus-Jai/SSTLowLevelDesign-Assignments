# Multilevel Parking Lot

## Design

- `VehicleType` — TWO_WHEELER, CAR, BUS
- `SlotType` — SMALL, MEDIUM, LARGE (with hourly rates)
- `Vehicle` — plate number + type
- `ParkingSlot` — id, type, floor number, position index, occupied flag
- `ParkingFloor` — floor number, list of slots
- `EntryGate` — id, floor number, position index
- `ParkingTicket` — vehicle, assigned slot, entry time
- `Bill` — ticket, exit time, computed amount
- `SlotFinder` — finds nearest available compatible slot from a given gate
- `ParkingLot` — exposes park(), status(), exit()

## Slot Compatibility
- TWO_WHEELER → SMALL, MEDIUM, LARGE (prefers smallest)
- CAR         → MEDIUM, LARGE (prefers smallest)
- BUS         → LARGE only

## Billing
Charged by allocated slot type, not vehicle type.
- SMALL  → ₹20/hr
- MEDIUM → ₹40/hr
- LARGE  → ₹80/hr

## Build & Run
```
cd "Multilevel Parking Lot/src"
javac com/example/parking/*.java
java com.example.parking.Main
```
