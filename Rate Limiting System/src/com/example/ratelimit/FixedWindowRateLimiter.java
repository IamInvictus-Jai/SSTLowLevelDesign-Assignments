package com.example.ratelimit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixed Window Counter algorithm.
 *
 * Time is divided into fixed buckets of windowDuration.
 * Each key gets a counter that resets at the start of each new window.
 *
 * Trade-off:
 *   + O(1) time and space per key
 *   - Boundary burst: a client can make 2x the limit by sending requests
 *     at the end of one window and the start of the next.
 *
 * Thread-safety: per-key synchronization via WindowEntry monitors.
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, WindowEntry> windows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    @Override
    public String getName() { return "FixedWindow"; }

    @Override
    public boolean allowRequest(String key) {
        WindowEntry entry = windows.computeIfAbsent(key, k -> new WindowEntry());
        synchronized (entry) {
            long now = System.currentTimeMillis();
            long windowMs = config.getWindow().toMillis();

            // Reset counter if we've moved into a new window
            if (now - entry.windowStart >= windowMs) {
                entry.windowStart = now;
                entry.count = 0;
            }

            if (entry.count < config.getLimit()) {
                entry.count++;
                return true;
            }
            return false;
        }
    }

    @Override
    public int getCurrentCount(String key) {
        WindowEntry entry = windows.get(key);
        if (entry == null) return 0;
        synchronized (entry) {
            long now = System.currentTimeMillis();
            if (now - entry.windowStart >= config.getWindow().toMillis()) return 0;
            return entry.count;
        }
    }

    private static class WindowEntry {
        long windowStart = System.currentTimeMillis();
        int count = 0;
    }
}
