package com.example.ratelimit;

/**
 * Factory for creating RateLimiter instances.
 * Callers switch algorithms here — business logic never changes.
 */
public class RateLimiterFactory {

    public static RateLimiter create(RateLimiterAlgorithm algorithm, RateLimiterConfig config) {
        switch (algorithm) {
            case FIXED_WINDOW:   return new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW: return new SlidingWindowRateLimiter(config);
            default: throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}
