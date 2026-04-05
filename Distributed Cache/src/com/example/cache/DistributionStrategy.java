package com.example.cache;

import java.util.List;

/**
 * Strategy interface for deciding which cache node stores a given key.
 * Implementations: ModuloStrategy, ConsistentHashingStrategy, etc.
 */
public interface DistributionStrategy {
    String getName();

    /**
     * Returns the index of the node responsible for the given key.
     * @param key   the cache key
     * @param nodes the current list of nodes
     */
    int getNodeIndex(String key, List<CacheNode> nodes);
}
