package com.opaleio.covid19stats.covid.domain;

import java.time.LocalDate;

public class DailyCovidStat {

    private LocalDate day;
    private String country;
    private Long confirmed;
    private Long death;
    private Long recovered;
    private Coordinates coordinates;

    public DailyCovidStat(LocalDate day, String country, Long confirmed, Long death, Long recovered, Coordinates coordinates) {
        this.day = day;
        this.country = country;
        this.confirmed = confirmed;
        this.death = death;
        this.recovered = recovered;
        this.coordinates = coordinates;
    }

    public LocalDate getDay() {
        return day;
    }

    public String getCountry() {
        return country;
    }

    public Long getConfirmed() {
        return confirmed;
    }

    public Long getDeath() {
        return death;
    }

    public Long getRecovered() {
        return recovered;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "DailyCovidStat{" +
                "day=" + day +
                ", country='" + country + '\'' +
                ", confirmed=" + confirmed +
                ", death=" + death +
                ", recovered=" + recovered +
                ", coordinates=" + coordinates +
                '}';
    }
}
