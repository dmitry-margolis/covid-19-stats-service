package com.accenture.covid19stats.service;

import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;

import java.util.List;

public interface AnalysisService {
    List<CountrySummary> getCountrySummaries(CountryFilter filter);
    VaccinesToDeathsPCC getVaccinesToDeathsPCC(CountryFilter filter);
}
