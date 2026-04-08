package com.example.ratelimit;

import java.time.Duration;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Pluggable Rate Limiting System Demo ===\n");

        RateLimiterConfig config = new RateLimiterConfig(5, Duration.ofSeconds(10));
        System.out.println("Config: " + config + "\n");

        ExternalResourceClient externalClient = new ExternalResourceClient("PaymentAPI");

        // =====================================================================
        // Scenario 1: Fixed Window — 5 requests per 10 seconds
        // =====================================================================
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("  Scenario 1: Fixed Window Counter");
        System.out.println("╚══════════════════════════════════════════╝");

        RateLimiter fixedLimiter = RateLimiterFactory.create(RateLimiterAlgorithm.FIXED_WINDOW, config);
        InternalService service = new InternalService(fixedLimiter, externalClient);

        // T1 makes 7 requests — first 5 should pass, last 2 denied
        System.out.println("\n-- T1 makes 7 requests (limit=5) --");
        for (int i = 1; i <= 7; i++) {
            String result = service.handleRequest("T1", true, "payload-" + i);
            System.out.println("  → result: " + result + "\n");
        }

        // T2 is a different key — has its own independent counter
        System.out.println("-- T2 makes 3 requests (independent key) --");
        for (int i = 1; i <= 3; i++) {
            String result = service.handleRequest("T2", true, "t2-payload-" + i);
            System.out.println("  → result: " + result + "\n");
        }

        // Request that doesn't need external call — no rate limiting applied
        System.out.println("-- T1 makes a request that doesn't need external call --");
        service.handleRequest("T1", false, "ignored");
        System.out.println();

        // =====================================================================
        // Scenario 2: Switch to Sliding Window — same config, no code change
        // =====================================================================
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("  Scenario 2: Sliding Window Counter");
        System.out.println("  (algorithm swapped, business logic unchanged)");
        System.out.println("╚══════════════════════════════════════════╝\n");

        RateLimiter slidingLimiter = RateLimiterFactory.create(RateLimiterAlgorithm.SLIDING_WINDOW, config);
        InternalService service2 = service.withRateLimiter(slidingLimiter);

        System.out.println("-- T3 makes 7 requests with Sliding Window --");
        for (int i = 1; i <= 7; i++) {
            String result = service2.handleRequest("T3", true, "sw-payload-" + i);
            System.out.println("  → result: " + result + "\n");
        }

        // =====================================================================
        // Scenario 3: Window reset — wait for window to expire, then retry
        // =====================================================================
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("  Scenario 3: Window reset after expiry");
        System.out.println("╚══════════════════════════════════════════╝\n");

        RateLimiterConfig shortConfig = new RateLimiterConfig(3, Duration.ofSeconds(2));
        RateLimiter shortFixed = RateLimiterFactory.create(RateLimiterAlgorithm.FIXED_WINDOW, shortConfig);
        InternalService service3 = new InternalService(shortFixed, externalClient);

        System.out.println("-- T4: 3 requests (all pass), then 2 more (denied) --");
        for (int i = 1; i <= 5; i++) {
            service3.handleRequest("T4", true, "reset-" + i);
        }

        System.out.println("\n-- Waiting 2 seconds for window to reset... --");
        Thread.sleep(2100);

        System.out.println("\n-- T4: 3 more requests after window reset (should all pass) --");
        for (int i = 6; i <= 8; i++) {
            service3.handleRequest("T4", true, "reset-" + i);
        }

        // =====================================================================
        // Scenario 4: Sliding window — boundary burst prevention
        // =====================================================================
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("  Scenario 4: Sliding window boundary accuracy");
        System.out.println("╚══════════════════════════════════════════╝\n");

        RateLimiterConfig burstConfig = new RateLimiterConfig(3, Duration.ofSeconds(2));
        RateLimiter slidingShort = RateLimiterFactory.create(RateLimiterAlgorithm.SLIDING_WINDOW, burstConfig);
        InternalService service4 = new InternalService(slidingShort, externalClient);

        System.out.println("-- T5: 3 requests (fills window) --");
        for (int i = 1; i <= 3; i++) service4.handleRequest("T5", true, "burst-" + i);

        System.out.println("\n-- T5: 1 more request immediately (should be denied) --");
        service4.handleRequest("T5", true, "burst-4");

        System.out.println("\n-- Waiting 2.1 seconds for oldest timestamps to expire... --");
        Thread.sleep(2100);

        System.out.println("\n-- T5: 3 more requests (window slid, should pass) --");
        for (int i = 5; i <= 7; i++) service4.handleRequest("T5", true, "burst-" + i);

        System.out.println("\n-- Total external calls made: " + externalClient.getTotalCallsMade() + " --");
    }
}
