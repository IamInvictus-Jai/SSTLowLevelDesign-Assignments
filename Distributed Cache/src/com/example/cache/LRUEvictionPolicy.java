package com.example.cache;

import java.util.LinkedHashMap;

/**
 * Least Recently Used eviction policy.
 *
 * Uses a LinkedHashMap in access-order mode.
 * The eldest entry (head) is always the least recently used.
 * All operations are O(1).
 */
public class LRUEvictionPolicy implements EvictionPolicy {

    // access-order=true: get/put moves entry to tail (most recent)
    private final LinkedHashMap<String, Boolean> order =
            new LinkedHashMap<>(16, 0.75f, true);

    @Override
    public String getName() { return "LRU"; }

    @Override
    public void onAccess(String key) {
        // LinkedHashMap access-order handles this automatically on get()
        order.get(key); // touch to move to tail
    }

    @Override
    public void onInsert(String key) {
        order.put(key, Boolean.TRUE);
    }

    @Override
    public void onRemove(String key) {
        order.remove(key);
    }

    @Override
    public String evict() {
        if (order.isEmpty()) throw new IllegalStateException("Nothing to evict.");
        // Head of access-ordered LinkedHashMap = least recently used
        return order.entrySet().iterator().next().getKey();
    }
}
