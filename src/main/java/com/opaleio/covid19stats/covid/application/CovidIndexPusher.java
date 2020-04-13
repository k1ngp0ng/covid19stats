package com.opaleio.covid19stats.covid.application;

import com.opaleio.covid19stats.covid.domain.DailyCovidStat;
import com.opaleio.covid19stats.covid.infra.CovidStatsIndex;
import com.opaleio.covid19stats.elastic.ElasticClient;
import com.opaleio.covid19stats.elastic.model.ElasticIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CovidIndexPusher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CovidIndexPusher.class);

    private final CovidStatsLoader covidStatsLoader;
    private final ElasticClient elasticClient;

    public CovidIndexPusher(CovidStatsLoader covidStatsLoader, ElasticClient elasticClient) {
        this.covidStatsLoader = covidStatsLoader;
        this.elasticClient = elasticClient;
    }

    public void pushCovidSats() {
        List<DailyCovidStat> dailyCovidStats = covidStatsLoader.loadStats();

        List<ElasticIndex> elasticCovidStats = dailyCovidStats.stream()
                .map(this::buildIndex)
                .collect(toList());
        elasticClient.bulkInsert(elasticCovidStats, 1000, "covidstats");

        LOGGER.info("Import finished");
    }

    private CovidStatsIndex buildIndex(DailyCovidStat dailyCovidStat) {
        return new CovidStatsIndex(dailyCovidStat.day(), dailyCovidStat.country(), dailyCovidStat.region(), dailyCovidStat.confirmed(),
                dailyCovidStat.death(), dailyCovidStat.recovered(), dailyCovidStat.coordinates());
    }

}
