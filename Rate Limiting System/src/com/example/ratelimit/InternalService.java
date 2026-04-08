package com.example.ratelimit;

/**
 * Internal service that may or may not call the external resource.
 *
 * Key design point: rate limiting is applied ONLY at the point of the external call,
 * not at the API entry point. Business logic always runs.
 */
public class InternalService {
    private final RateLimiter rateLimiter;
    private final ExternalResourceClient externalClient;

    public InternalService(RateLimiter rateLimiter, ExternalResourceClient externalClient) {
        this.rateLimiter = rateLimiter;
        this.externalClient = externalClient;
    }

    /**
     * Handles a client request.
     *
     * @param key            rate limiting key (customer ID, tenant ID, API key, etc.)
     * @param needsExternal  whether this request requires an external resource call
     * @param payload        data to send to the external resource
     */
    public String handleRequest(String key, boolean needsExternal, String payload) {
        // Business logic always runs regardless of rate limiting
        System.out.println("[Service] Handling request for key=\"" + key
                + "\" needsExternal=" + needsExternal);

        if (!needsExternal) {
            System.out.println("[Service] No external call needed. Returning local result.");
            return "local_result";
        }

        // Rate limit check — only at the point of external call
        if (!rateLimiter.allowRequest(key)) {
            System.out.println("[Service] RATE LIMITED key=\"" + key
                    + "\" count=" + rateLimiter.getCurrentCount(key)
                    + "/" + "limit reached. External call denied.");
            return "rate_limited";
        }

        System.out.println("[Service] Allowed (count=" + rateLimiter.getCurrentCount(key)
                + "). Making external call...");
        return externalClient.call(payload);
    }

    /** Swap the rate limiter at runtime without changing any business logic. */
    public InternalService withRateLimiter(RateLimiter newLimiter) {
        return new InternalService(newLimiter, externalClient);
    }
}
