package com.example.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Top-level distributed cache.
 *
 * Responsibilities:
 * - Route get/put to the correct node via DistributionStrategy
 * - Handle cache misses by fetching from Database and populating the node
 * - Allow strategy to be swapped at runtime
 *
 * Assumption: put(key, value) also persists to the database (write-through).
 */
public class DistributedCache {
    private final List<CacheNode> nodes;
    private DistributionStrategy strategy;
    private final Database database;

    public DistributedCache(int nodeCount, int capacityPerNode,
                             DistributionStrategy strategy, Database database) {
        this.strategy = strategy;
        this.database = database;
        this.nodes = new ArrayList<>(nodeCount);

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new CacheNode("N" + i, capacityPerNode, new LRUEvictionPolicy()));
        }

        System.out.println("[Cache] Initialized: " + nodeCount + " nodes, capacity="
                + capacityPerNode + " each, strategy=" + strategy.getName());
    }

    // -------------------------------------------------------------------------
    // get(key)
    // -------------------------------------------------------------------------
    public String get(String key) {
        CacheNode node = routeTo(key);
        String value = node.get(key);

        if (value != null) {
            System.out.println("[Cache] HIT  key=\"" + key + "\" → \"" + value
                    + "\" (node=" + node.getId() + ")");
            return value;
        }

        // Cache miss — fetch from DB, populate cache
        System.out.println("[Cache] MISS key=\"" + key + "\" (node=" + node.getId() + ") — fetching from DB...");
        value = database.fetch(key);

        if (value != null) {
            node.put(key, value);
            System.out.println("[Cache] Populated key=\"" + key + "\" into node=" + node.getId());
        } else {
            System.out.println("[Cache] Key \"" + key + "\" not found in DB either.");
        }

        return value;
    }

    // -------------------------------------------------------------------------
    // put(key, value)
    // -------------------------------------------------------------------------
    public void put(String key, String value) {
        CacheNode node = routeTo(key);
        node.put(key, value);
        database.save(key, value); // write-through
        System.out.println("[Cache] PUT  key=\"" + key + "\" → \"" + value
                + "\" (node=" + node.getId() + ")");
    }

    // -------------------------------------------------------------------------
    // Strategy swap at runtime
    // -------------------------------------------------------------------------
    public void setStrategy(DistributionStrategy strategy) {
        this.strategy = strategy;
        System.out.println("[Cache] Distribution strategy switched to: " + strategy.getName());
    }

    // -------------------------------------------------------------------------
    // Diagnostics
    // -------------------------------------------------------------------------
    public void printStatus() {
        System.out.println("[Cache] === Node Status ===");
        nodes.forEach(CacheNode::printContents);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------
    private CacheNode routeTo(String key) {
        int index = strategy.getNodeIndex(key, nodes);
        return nodes.get(index);
    }

    public List<CacheNode> getNodes() { return Collections.unmodifiableList(nodes); }
}
