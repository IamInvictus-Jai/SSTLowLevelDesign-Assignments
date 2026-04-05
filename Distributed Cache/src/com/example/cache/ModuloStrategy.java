package com.example.cache;

import java.util.List;

/**
 * Simple modulo-based distribution: hash(key) % numberOfNodes.
 * Uses Math.abs to handle negative hash codes.
 */
public class ModuloStrategy implements DistributionStrategy {

    @Override
    public String getName() { return "Modulo"; }

    @Override
    public int getNodeIndex(String key, List<CacheNode> nodes) {
        return Math.abs(key.hashCode()) % nodes.size();
    }
}
