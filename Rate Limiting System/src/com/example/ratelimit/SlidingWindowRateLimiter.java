package com.example.ratelimit;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Counter algorithm.
 *
 * Maintains a queue of timestamps for each key.
 * On every request, expired timestamps (older than windowDuration) are pruned,
 * then the current count is checked against the limit.
 *
 * Trade-off:
 *   + Accurate rolling window — no boundary burst problem
 *   - O(n) space per key where n = requests in the window
 *   - O(n) cleanup per request in the worst case (amortized O(1) in practice)
 *
 * Thread-safety: per-key synchronization via TimestampLog monitors.
 */
public class SlidingWindowRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, TimestampLog> logs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    @Override
    public String getName() { return "SlidingWindow"; }

    @Override
    public boolean allowRequest(String key) {
        TimestampLog log = logs.computeIfAbsent(key, k -> new TimestampLog());
        synchronized (log) {
            long now = System.currentTimeMillis();
            long windowMs = config.getWindow().toMillis();

            // Evict timestamps outside the rolling window
            while (!log.timestamps.isEmpty() && now - log.timestamps.peekFirst() >= windowMs) {
                log.timestamps.pollFirst();
            }

            if (log.timestamps.size() < config.getLimit()) {
                log.timestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    @Override
    public int getCurrentCount(String key) {
        TimestampLog log = logs.get(key);
        if (log == null) return 0;
        synchronized (log) {
            long now = System.currentTimeMillis();
            long windowMs = config.getWindow().toMillis();
            // Count only timestamps within the window (don't mutate here)
            return (int) log.timestamps.stream()
                    .filter(t -> now - t < windowMs)
                    .count();
        }
    }

    private static class TimestampLog {
        final Deque<Long> timestamps = new ArrayDeque<>();
    }
}
