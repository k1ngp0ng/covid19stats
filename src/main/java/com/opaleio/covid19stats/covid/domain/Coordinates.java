package com.opaleio.covid19stats.covid.domain;

public class Coordinates {

    private final Double latitude;
    private final Double longitude;

    private Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;    }

    public static Coordinates of(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }

    public Double getLat() {
        return latitude;
    }

    public Double getLon() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("(lat:%s, long:%s)", latitude, longitude);
    }
}
