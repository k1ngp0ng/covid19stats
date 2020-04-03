package com.opaleio.covid19stats.covid.api;

import com.opaleio.covid19stats.covid.application.CovidIndexPusher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/covid")
public class CovidApi {

    private final CovidIndexPusher covidIndexPusher;

    public CovidApi(CovidIndexPusher covidIndexPusher) {
        this.covidIndexPusher = covidIndexPusher;
    }

    @GetMapping("/load")
    public void loadIndex() {
        covidIndexPusher.pushCovidSats();
    }
}
