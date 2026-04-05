package com.example.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * A single cache node with fixed capacity and a pluggable eviction policy.
 *
 * Responsibilities:
 * - Store key-value pairs up to capacity
 * - Evict entries via the policy when full
 * - Notify the policy on every access/insert/remove
 */
public class CacheNode {
    private final String id;
    private final int capacity;
    private final EvictionPolicy evictionPolicy;
    private final Map<String, String> store = new HashMap<>();

    public CacheNode(String id, int capacity, EvictionPolicy evictionPolicy) {
        this.id = id;
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
    }

    /**
     * Returns the value for the key, or null if not present.
     */
    public String get(String key) {
        if (!store.containsKey(key)) return null;
        evictionPolicy.onAccess(key);
        return store.get(key);
    }

    /**
     * Inserts or updates a key-value pair.
     * Evicts if at capacity and key is new.
     */
    public void put(String key, String value) {
        if (store.containsKey(key)) {
            // Update existing — just refresh access order
            store.put(key, value);
            evictionPolicy.onAccess(key);
        } else {
            if (store.size() >= capacity) {
                evict();
            }
            store.put(key, value);
            evictionPolicy.onInsert(key);
        }
    }

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    public int size()     { return store.size(); }
    public int capacity() { return capacity; }
    public String getId() { return id; }
    public String getEvictionPolicyName() { return evictionPolicy.getName(); }

    private void evict() {
        String victim = evictionPolicy.evict();
        store.remove(victim);
        evictionPolicy.onRemove(victim);
        System.out.println("[Node-" + id + "] Evicted (policy=" + evictionPolicy.getName() + "): \"" + victim + "\"");
    }

    public void printContents() {
        System.out.println("[Node-" + id + "] capacity=" + capacity
                + " size=" + store.size() + " policy=" + evictionPolicy.getName()
                + " contents=" + store);
    }
}
