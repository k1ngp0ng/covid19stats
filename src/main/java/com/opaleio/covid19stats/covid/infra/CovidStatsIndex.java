package com.opaleio.covid19stats.covid.infra;

import com.opaleio.covid19stats.covid.domain.Coordinates;
import com.opaleio.covid19stats.elastic.model.ElasticIndex;

import java.time.LocalDate;
import java.util.UUID;

public class CovidStatsIndex implements ElasticIndex {

    private final LocalDate day;
    private final String country;
    private final String region;
    private final Long confirmed;
    private final Long death;
    private final Long recovered;
    private final Coordinates location;

    public CovidStatsIndex(LocalDate day, String country, String region, Long confirmed, Long death, Long recovered, Coordinates coordinates) {
        this.day = day;
        this.confirmed = confirmed;
        this.country = country;
        this.region = region;
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

    public String getRegion() {
        return region;
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

    public Long getInfected() {
        return (confirmed - recovered - death);
    }

    public Coordinates getLocation() {
        return location;
    }
}
