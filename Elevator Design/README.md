# Elevator System Design

## Classes

| Class | Responsibility |
|---|---|
| `ElevatorCar` | Single car — state, floor, weight, destination queue, door control |
| `InternalPanel` | Inside-car buttons: floor numbers, open, close, alarm |
| `ExternalPanel` | Per-floor buttons: UP, DOWN — routes to controller |
| `ElevatorController` | Manages all cars, dispatches floor requests via strategy |
| `Building` | Holds floors and their external panels |
| `DispatchStrategy` | Interface — `selectCar(request, cars)` |
| `FCFSStrategy` | First Come First Serve — picks first non-maintenance car |
| `ShortestPathStrategy` | Picks car with minimum distance, prefers IDLE then same-direction |
| `FloorRequest` | External request: floor + direction |
| `CabinRequest` | Internal request: destination floor |

## State Machine (ElevatorCar)
```
IDLE ──► MOVING_UP / MOVING_DOWN  (when destination queued)
MOVING_UP / MOVING_DOWN ──► IDLE  (when queue empty)
Any ──► MAINTENANCE               (setMaintenance() or alarm)
MAINTENANCE ──► IDLE              (clearMaintenance())
```

## Key Design Decisions
- **Strategy pattern** for dispatch — swap `FCFSStrategy` ↔ `ShortestPathStrategy` (or any new one) at runtime via `controller.setStrategy()`
- **TreeSet** for destination queue — O(log n) insert, efficient nearest-floor lookup
- **Weight limit** — 700 KG enforced on `addWeight()`, cabin requests can check before boarding
- **Alarm** — clears destination queue, sets MAINTENANCE, opens door at current floor
- **Maintenance** — cars in MAINTENANCE are excluded from all dispatch strategies

## Build & Run
```
cd "Elevator Design/src"
javac com/example/elevator/*.java
java com.example.elevator.Main
```
