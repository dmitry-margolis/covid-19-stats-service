package com.accenture.covid19stats.controller;

import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analysis")
public class AnalysisController {
    private final AnalysisService service;

    @GetMapping("/summary")
    public List<CountrySummary> getCountrySummaries(@RequestParam(name = "continent", required = false) String continent) {
        return service.getCountrySummaries(CountryFilter.builder()
                .continent(continent)
                .build());
    }

    @GetMapping("/vaccines-to-deaths-pcc")
    public VaccinesToDeathsPCC getVaccinesToDeathsPCC(@RequestParam(name = "continent", required = false) String continent) {
        if(true) {
            throw new NullPointerException();
        }
        return service.getVaccinesToDeathsPCC(CountryFilter.builder()
                .continent(continent)
                .build());
    }
}
