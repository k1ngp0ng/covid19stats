package com.opaleio.covid19stats.covid.infra;

import com.opaleio.covid19stats.covid.domain.DailyCovidStat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CovidStatsCsvRepositoryTest {

    @Test
    void testLoadDailyCsvFormat1() {
        CovidStatsCsvRepository covidStatsCsvRepository = new CovidStatsCsvRepository("src/test/resources");
        List<DailyCovidStat> dailyCovidStatList = covidStatsCsvRepository.loadDailyStats(LocalDate.of(2020, 02,29));
        assertThat(dailyCovidStatList).hasSize(119);

    }

    @Test
    void testLoadDailyCsvFormat2() {
        CovidStatsCsvRepository covidStatsCsvRepository = new CovidStatsCsvRepository("src/test/resources");
        List<DailyCovidStat> dailyCovidStatList = covidStatsCsvRepository.loadDailyStats(LocalDate.of(2020, 03,22));
        assertThat(dailyCovidStatList).hasSize(3417);

    }

    @Test
    void testLoadAllRepo() {
        CovidStatsCsvRepository covidStatsCsvRepository = new CovidStatsCsvRepository("src/test/resources");
        List<DailyCovidStat> dailyCovidStatList = covidStatsCsvRepository.loadStats();
        assertThat(dailyCovidStatList).hasSize(3536);

    }


}