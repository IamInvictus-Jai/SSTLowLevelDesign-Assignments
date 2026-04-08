package com.example.ratelimit;

/**
 * Core abstraction for all rate limiting algorithms.
 *
 * Internal services call allowRequest(key) before making any external resource call.
 * The key can be a customer ID, tenant ID, API key, or provider name.
 *
 * Implementations must be thread-safe.
 */
public interface RateLimiter {
    String getName();

    /**
     * Returns true if the request is within the allowed rate limit.
     * Returns false if the limit has been exceeded — caller should deny the external call.
     */
    boolean allowRequest(String key);

    /** Returns current request count within the active window for the given key. */
    int getCurrentCount(String key);
}
