package com.example.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight factory — caches and returns shared MarkerStyle instances.
 * Identical style configs always return the same object reference.
 */
public class MarkerStyleFactory {

    private final Map<String, MarkerStyle> cache = new HashMap<>();

    public MarkerStyle get(String shape, String color, int size, boolean filled) {
        String key = shape + "|" + color + "|" + size + "|" + (filled ? "F" : "O");
        return cache.computeIfAbsent(key, k -> new MarkerStyle(shape, color, size, filled));
    }

    public int cacheSize() {
        return cache.size();
    }
}
