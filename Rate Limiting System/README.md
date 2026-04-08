# Pluggable Rate Limiting System

## Design

### Classes

| Class | Responsibility |
|---|---|
| `RateLimiter` | Interface — `allowRequest(key)`, `getCurrentCount(key)` |
| `RateLimiterConfig` | Immutable config — limit + window duration |
| `FixedWindowRateLimiter` | Fixed bucket counter, resets at window boundary |
| `SlidingWindowRateLimiter` | Rolling timestamp queue, accurate window |
| `RateLimiterAlgorithm` | Enum — FIXED_WINDOW, SLIDING_WINDOW (extensible) |
| `RateLimiterFactory` | Creates the right implementation — swap algorithm here |
| `InternalService` | Business logic + rate limit check only at external call point |
| `ExternalResourceClient` | Simulates the paid external API |

---

### Key Design Decisions

**Rate limiting is NOT at the API entry point.**
`InternalService.handleRequest()` runs business logic first. Only if `needsExternal=true` does it consult the rate limiter. Requests that don't need the external resource are never counted.

**Per-key isolation.**
Each key (customer ID, tenant ID, API key) has its own independent counter/window. T1 being rate-limited has zero effect on T2.

**Algorithm is pluggable.**
`RateLimiterFactory.create(algorithm, config)` is the only place that knows about concrete implementations. `InternalService` depends only on the `RateLimiter` interface. Switching algorithms requires zero changes to business logic.

**Thread-safety.**
Both implementations use `ConcurrentHashMap` for key-level storage and `synchronized` on per-key entry objects — fine-grained locking, no global bottleneck.

---

### Algorithm Trade-offs

| | Fixed Window | Sliding Window |
|---|---|---|
| Memory | O(1) per key | O(n) per key (n = requests in window) |
| Time | O(1) per request | O(n) cleanup, amortized O(1) |
| Accuracy | Boundary burst possible (2x limit at window edge) | Accurate rolling window, no burst |
| Simplicity | Very simple | Slightly more complex |
| Best for | High-throughput, burst tolerance acceptable | Strict per-window accuracy required |

---

### Adding a New Algorithm
1. Implement `RateLimiter`
2. Add entry to `RateLimiterAlgorithm` enum
3. Add case to `RateLimiterFactory.create()`

No other code changes needed.

## Build & Run
```
cd "Rate Limiting System/src"
javac com/example/ratelimit/*.java
java com.example.ratelimit.Main
```
