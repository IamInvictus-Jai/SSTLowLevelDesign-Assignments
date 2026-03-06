package com.example.map;

/**
 * Holds extrinsic state (lat, lng, label) per marker instance.
 * Intrinsic state (style) is a shared MarkerStyle from MarkerStyleFactory.
 */
public class MapMarker {

    private final double lat;
    private final double lng;
    private final String label;
    private final MarkerStyle style; // shared flyweight

    public MapMarker(double lat, double lng, String label, MarkerStyle style) {
        this.lat = lat;
        this.lng = lng;
        this.label = label;
        this.style = style;
    }

    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public String getLabel() { return label; }
    public MarkerStyle getStyle() { return style; }
}
