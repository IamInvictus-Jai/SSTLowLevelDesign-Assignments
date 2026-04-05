package com.example.cache;

/**
 * Strategy interface for cache eviction.
 * Implementations: LRUEvictionPolicy, MRUEvictionPolicy, LFUEvictionPolicy, etc.
 *
 * The policy maintains its own internal tracking structure.
 * CacheNode calls these methods on every access and mutation.
 */
public interface EvictionPolicy {
    String getName();

    /** Called when a key is accessed (get or put). */
    void onAccess(String key);

    /** Called when a new key is inserted. */
    void onInsert(String key);

    /** Called when a key is explicitly removed. */
    void onRemove(String key);

    /**
     * Returns the key that should be evicted next according to this policy.
     * CacheNode will call onRemove() after eviction.
     */
    String evict();
}
