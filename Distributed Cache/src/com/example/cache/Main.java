package com.example.cache;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Distributed Cache Demo ===\n");

        Database db = new FakeDatabase();
        DistributedCache cache = new DistributedCache(3, 3, new ModuloStrategy(), db);
        System.out.println();

        // ---- Scenario 1: Cache miss → DB fetch → populate ----
        System.out.println("--- Scenario 1: Cache misses (first access) ---");
        cache.get("user:1");    // miss → DB → populate
        cache.get("user:2");    // miss → DB → populate
        cache.get("product:1"); // miss → DB → populate
        System.out.println();

        // ---- Scenario 2: Cache hit (same keys again) ----
        System.out.println("--- Scenario 2: Cache hits (same keys) ---");
        cache.get("user:1");
        cache.get("product:1");
        System.out.println();

        // ---- Scenario 3: put() — write-through ----
        System.out.println("--- Scenario 3: put() — write-through to DB ---");
        cache.put("user:99", "Zara");
        cache.get("user:99"); // should be a hit now
        System.out.println();

        // ---- Scenario 4: Key not in cache or DB ----
        System.out.println("--- Scenario 4: Key not found anywhere ---");
        cache.get("unknown:key");
        System.out.println();

        // ---- Scenario 5: LRU eviction ----
        // Each node has capacity=3. Fill a node then add one more to trigger eviction.
        // All keys below route to the same node (node index depends on hash % 3).
        // We'll fill node N0 by finding 4 keys that hash to it.
        System.out.println("--- Scenario 5: LRU eviction ---");
        System.out.println("Filling nodes to trigger eviction...");

        // Access user:1 and user:2 to make them recently used
        cache.get("user:1");
        cache.get("user:2");

        // Now add more keys to the same nodes to trigger eviction
        cache.put("order:1", "Order#1001");
        cache.put("order:2", "Order#1002");
        cache.put("order:3", "Order#9999"); // may trigger eviction on whichever node is full
        cache.put("product:2", "Phone");
        cache.put("product:3", "Tablet");
        System.out.println();

        // ---- Status after evictions ----
        System.out.println("--- Node status after evictions ---");
        cache.printStatus();
        System.out.println();

        // ---- Scenario 6: Verify evicted key is re-fetched from DB ----
        System.out.println("--- Scenario 6: Access potentially evicted key (re-fetch from DB) ---");
        cache.get("user:3");    // may have been evicted — should re-fetch from DB
        cache.get("product:1"); // may have been evicted — should re-fetch from DB
        System.out.println();

        // ---- Final status ----
        System.out.println("--- Final node status ---");
        cache.printStatus();
    }
}
