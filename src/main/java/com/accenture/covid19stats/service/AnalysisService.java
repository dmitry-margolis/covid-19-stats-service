package com.accenture.covid19stats.service;

import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.model.VaccinesToDeathsPCC;

import java.util.List;

/**
 * Service for covid-19 data analysis
 */
public interface AnalysisService {
    /**
     * Summary statistics for covid-19 cases and vaccination for selected countries (cases + vaccination)
     */
    List<CountrySummary> getCountrySummaries(CountryFilter filter);

    /**
     * Pearson correlation coefficient between vaccination and fatality rates
     */
    VaccinesToDeathsPCC getVaccinesToDeathsPCC(CountryFilter filter);
}