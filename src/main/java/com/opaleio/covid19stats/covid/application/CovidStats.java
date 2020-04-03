package com.opaleio.covid19stats.covid.application;

import com.opaleio.covid19stats.covid.domain.DailyCovidStat;

import java.util.List;

public interface CovidStats {

     List<DailyCovidStat> loadStats();
}
