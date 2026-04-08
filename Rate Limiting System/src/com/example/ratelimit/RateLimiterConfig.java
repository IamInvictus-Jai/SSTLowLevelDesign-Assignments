package com.example.ratelimit;

import java.time.Duration;

/**
 * Immutable configuration for a rate limiter.
 * Example: limit=100, window=1 minute → 100 requests per minute.
 */
public class RateLimiterConfig {
    private final int limit;
    private final Duration window;

    public RateLimiterConfig(int limit, Duration window) {
        if (limit <= 0) throw new IllegalArgumentException("Limit must be > 0");
        if (window == null || window.isNegative() || window.isZero())
            throw new IllegalArgumentException("Window must be a positive duration");
        this.limit = limit;
        this.window = window;
    }

    /** Maximum number of requests allowed within the window. */
    public int getLimit()      { return limit; }

    /** Duration of the rate limiting window. */
    public Duration getWindow() { return window; }

    @Override
    public String toString() {
        return limit + " requests / " + window.toSeconds() + "s";
    }
}
