# Movie Ticket Booking System

## Design Overview

### Entities
| Class | Responsibility |
|---|---|
| `User` | Customer with unique email |
| `Movie` | Title, duration, genre |
| `Theatre` | Name, city, list of halls |
| `Hall` | Screen inside a theatre, holds seats |
| `Seat` | Row/number/type with base price |
| `Show` | A movie scheduled in a hall at a time |
| `ShowSeat` | Per-show seat state: AVAILABLE / LOCKED / BOOKED + runtime price |
| `Booking` | Links user + show + seats, holds payment |
| `Payment` | Amount, mode (UPI/CARD/NET_BANKING), status |
| `Ticket` | Generated after successful payment |

### Pricing
| Class | Role |
|---|---|
| `PricingRule` | Interface — `apply(price, showSeat, show, context)` |
| `TimeSlotPricingRule` | +15% evening, +10% weekend |
| `DemandSurgePricingRule` | +25% above 70% occupancy, +50% above 90% |
| `PricingEngine` | Holds active rules, applies them in order, enforces floor = base price |

Admin picks which rules are active at runtime via `AdminService.activatePricingRule()`.

### Concurrency & Seat Locking
`ShowSeat` has three states:
- `AVAILABLE` — visible and selectable by all
- `LOCKED` — user is on payment screen (10-min TTL, auto-expires back to AVAILABLE)
- `BOOKED` — paid, permanently unavailable

All state mutations on `ShowSeat` are `synchronized`. Locking is atomic per seat.

### APIs

**Admin:**
- `addMovie(title, duration, genre)`
- `addTheatre(name, city)`
- `addHall(theatre, name, seatCounts, basePrices)`
- `addShow(movie, hall, theatre, startTime)`
- `activatePricingRule(rule)` / `deactivatePricingRule(name)`

**Customer:**
- `registerUser(name, email, phone)` — email must be unique
- `showTheatres(city)`
- `showMovies(city)`
- `showShowsForMovie(movieId, city)`
- `showShowsForTheatre(theatreId)`
- `getSeatMap(showId)` — shows live prices and availability
- `bookTickets(showId, seatIds, userId, paymentMode)` → `Ticket`
- `cancelBooking(bookingId)` — refund to original payment mode

## Seat Compatibility & Pricing
- Base price is per seat type per theatre (same for all movies)
- Final price = base price → apply active pricing rules → never below base
- Billing is by allocated seat type

## Build & Run
```
cd "Movie Ticket Booking/src"
javac com/example/booking/*.java
java com.example.booking.Main
```
