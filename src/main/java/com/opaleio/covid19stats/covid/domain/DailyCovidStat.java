package com.opaleio.covid19stats.covid.domain;

import java.time.LocalDate;

public class DailyCovidStat {

    private final LocalDate day;
    private final String country;
    private final String region;
    private final Long confirmed;
    private final Long death;
    private final Long recovered;
    private final Coordinates coordinates;

    public DailyCovidStat(LocalDate day, String country, String region, Long confirmed, Long death, Long recovered, Coordinates coordinates) {
        this.day = day;
        this.country = country;
        this.region = region;
        this.confirmed = confirmed;
        this.death = death;
        this.recovered = recovered;
        this.coordinates = coordinates;
    }

    public LocalDate day() {
        return day;
    }

    public String country() {
        return country;
    }

    public String region() {
        return region;
    }

    public Long confirmed() {
        return confirmed;
    }

    public Long death() {
        return death;
    }

    public Long recovered() {
        return recovered;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "DailyCovidStat{" +
                "day=" + day +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", confirmed=" + confirmed +
                ", death=" + death +
                ", recovered=" + recovered +
                ", coordinates=" + coordinates +
                '}';
    }
}
