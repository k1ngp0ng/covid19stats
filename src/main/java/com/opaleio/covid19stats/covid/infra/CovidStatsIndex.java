package com.opaleio.covid19stats.covid.infra;

import com.opaleio.covid19stats.covid.domain.Coordinates;
import com.opaleio.covid19stats.elastic.model.ElasticIndex;

import java.time.LocalDate;
import java.util.UUID;

public class CovidStatsIndex implements ElasticIndex {

    private LocalDate day;
    private String country;
    private Long confirmed;
    private Long death;
    private Long recovered;
    private Coordinates location;

    public CovidStatsIndex(LocalDate day, String country, Long confirmed, Long death, Long recovered, Coordinates coordinates) {
        this.day = day;
        this.confirmed = confirmed;
        this.country = country;
        this.death = death;
        this.recovered = recovered;
        this.location = coordinates;
    }

    @Override
    public String getReference() {
        return UUID.randomUUID().toString();
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

    public Coordinates getLocation() {
        return location;
    }

}
