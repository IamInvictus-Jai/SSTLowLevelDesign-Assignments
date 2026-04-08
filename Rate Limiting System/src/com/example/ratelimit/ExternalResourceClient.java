package com.example.ratelimit;

/**
 * Simulates a paid external resource (e.g. a third-party API).
 * In a real system this would make an HTTP call.
 */
public class ExternalResourceClient {
    private final String resourceName;
    private int totalCallsMade = 0;

    public ExternalResourceClient(String resourceName) {
        this.resourceName = resourceName;
    }

    public String call(String payload) {
        totalCallsMade++;
        System.out.println("  [ExternalResource:" + resourceName + "] Called with payload=\""
                + payload + "\" (total calls so far: " + totalCallsMade + ")");
        return "response_for_" + payload;
    }

    public int getTotalCallsMade() { return totalCallsMade; }
}
