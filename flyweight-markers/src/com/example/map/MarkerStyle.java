package com.example.map;

/**
 * Immutable Flyweight — intrinsic state shared across many MapMarkers.
 * Instances are obtained via MarkerStyleFactory, never created directly.
 */
public class MarkerStyle {

    private final String shape;
    private final String color;
    private final int size;
    private final boolean filled;

    public MarkerStyle(String shape, String color, int size, boolean filled) {
        this.shape = shape;
        this.color = color;
        this.size = size;
        this.filled = filled;
    }

    public String getShape() { return shape; }
    public String getColor() { return color; }
    public int getSize() { return size; }
    public boolean isFilled() { return filled; }

    @Override
    public String toString() {
        return shape + "|" + color + "|" + size + "|" + (filled ? "F" : "O");
    }
}
