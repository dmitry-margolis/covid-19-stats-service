package com.accenture.covid19stats.controller;

import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Summary statistics for covid-19 cases and vaccination for selected countries")
    @GetMapping("/summary")
    public List<CountrySummary> getCountrySummaries(
            @RequestParam(name = "continent", required = false)
            @Parameter(description = "Countries continent. All countries selected if empty", example = "Europe")
            String continent) {
        return service.getCountrySummaries(CountryFilter.builder()
                .continent(continent)
                .build());
    }

    @Operation(summary = "Pearson correlation coefficient between vaccination and fatality rates")
    @GetMapping("/vaccines-to-deaths-pcc")
    public VaccinesToDeathsPCC getVaccinesToDeathsPCC(
            @RequestParam(name = "continent", required = false)
            @Parameter(description = "Countries continent. All countries selected if empty", example = "Europe")
            String continent) {
        return service.getVaccinesToDeathsPCC(CountryFilter.builder()
                .continent(continent)
                .build());
    }
}
