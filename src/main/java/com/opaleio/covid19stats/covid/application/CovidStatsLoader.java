package com.opaleio.covid19stats.covid.application;

import com.opaleio.covid19stats.covid.domain.DailyCovidStat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovidStatsLoader {

    private final CovidStats covidStats;

    public CovidStatsLoader(CovidStats covidStats) {
        this.covidStats = covidStats;
    }

    public List<DailyCovidStat> loadStats() {
        List<DailyCovidStat> dailyCovidStats = covidStats.loadStats();
        return dailyCovidStats;
    }
}
