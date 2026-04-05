package com.example.cache;

/**
 * Abstraction over the backing data store.
 * The distributed cache calls this on a cache miss.
 */
public interface Database {
    /**
     * Fetches the value for the given key from the database.
     * Returns null if the key does not exist.
     */
    String fetch(String key);

    /**
     * Persists a key-value pair to the database.
     */
    void save(String key, String value);
}
