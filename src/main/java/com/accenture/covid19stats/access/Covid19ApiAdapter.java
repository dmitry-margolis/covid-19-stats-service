package com.accenture.covid19stats.access;

import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.model.CountryFilter;

import java.util.Map;

public interface Covid19ApiAdapter {
    Map<String, CountryCasesDto> getAllCases();
    Map<String, CountryVaccinesDto> getAllVaccines();
    Map<String, CountryCasesDto> getCases(CountryFilter filter);
    Map<String, CountryVaccinesDto> getVaccines(CountryFilter filter);
}
