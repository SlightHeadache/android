package com.example.k1756.otto_etsin;

/**
 * Created by Juuso_2 on 8.10.2017.
 */

public class Coordinate {
    private double lat;
    private double lon;
    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
}
