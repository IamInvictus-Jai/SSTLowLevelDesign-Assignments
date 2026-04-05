# Distributed Cache

## Design

### Classes

| Class | Responsibility |
|---|---|
| `DistributedCache` | Top-level — routes get/put, handles cache miss, write-through to DB |
| `CacheNode` | Single node — stores key-value pairs, enforces capacity, delegates eviction |
| `DistributionStrategy` | Interface — decides which node owns a key |
| `ModuloStrategy` | `abs(hash(key)) % nodeCount` |
| `EvictionPolicy` | Interface — tracks access order, decides who gets evicted |
| `LRUEvictionPolicy` | Access-ordered `LinkedHashMap` — O(1) get/put/evict |
| `Database` | Interface — backing store abstraction |
| `FakeDatabase` | In-memory map, pre-seeded for demo |

---

### How data is distributed
`DistributionStrategy.getNodeIndex(key, nodes)` is called on every get/put.
`ModuloStrategy` uses `abs(key.hashCode()) % nodes.size()`.
To plug in consistent hashing: implement `DistributionStrategy`, pass to constructor or call `setStrategy()`.

### How cache miss is handled
1. Route key to node via strategy
2. `node.get(key)` returns null → cache miss
3. `database.fetch(key)` → value
4. `node.put(key, value)` → populate cache
5. Return value to caller

### How eviction works
`CacheNode` tracks size vs capacity. On insert when full:
1. `evictionPolicy.evict()` returns the victim key
2. Victim removed from store and policy tracking
3. New key inserted

`LRUEvictionPolicy` uses a `LinkedHashMap(accessOrder=true)`.
Every `get` and `put` moves the key to the tail. The head is always the LRU victim.

### Write policy
`put(key, value)` is **write-through** — updates both the cache node and the database.

### Extensibility
- New distribution strategy → implement `DistributionStrategy`, swap via `setStrategy()`
- New eviction policy → implement `EvictionPolicy`, pass to `CacheNode` constructor
- New database → implement `Database`, pass to `DistributedCache` constructor
- Node count and capacity → configurable at construction

## Build & Run
```
cd "Distributed Cache/src"
javac com/example/cache/*.java
java com.example.cache.Main
```
