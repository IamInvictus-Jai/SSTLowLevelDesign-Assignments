package com.example.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory fake database for demo/testing.
 * Pre-seeded with some data to demonstrate cache-miss population.
 */
public class FakeDatabase implements Database {
    private final Map<String, String> store = new HashMap<>();

    public FakeDatabase() {
        // Pre-seed some data
        store.put("user:1",    "Alice");
        store.put("user:2",    "Bob");
        store.put("user:3",    "Charlie");
        store.put("product:1", "Laptop");
        store.put("product:2", "Phone");
        store.put("product:3", "Tablet");
        store.put("order:1",   "Order#1001");
        store.put("order:2",   "Order#1002");
    }

    @Override
    public String fetch(String key) {
        String value = store.get(key);
        System.out.println("[DB] fetch(\"" + key + "\") → " + (value != null ? "\"" + value + "\"" : "null"));
        return value;
    }

    @Override
    public void save(String key, String value) {
        store.put(key, value);
        System.out.println("[DB] save(\"" + key + "\", \"" + value + "\")");
    }
}
