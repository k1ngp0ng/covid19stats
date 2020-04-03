package com.opaleio.covid19stats.covid.application;

import com.opaleio.covid19stats.covid.domain.DailyCovidStat;

import java.time.LocalDate;
import java.util.List;

public interface CovidStats {

     List<DailyCovidStat> loadStats();

     List<DailyCovidStat> loadDailyStats(LocalDate day);
}
