package com.accenture.covid19stats;

import com.accenture.covid19stats.access.Covid19ApiAdapterImpl;
import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.service.AnalysisService;
import com.accenture.covid19stats.service.AnalysisServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private Covid19ApiAdapterImpl covid19ApiAdapter;

    private AnalysisService statsService;

    @BeforeEach
    void init() {
        when(covid19ApiAdapter.getAllCases()).thenReturn(
                TestUtils.getTestDataFromJson("/cases-all.json", new TypeReference<>() {}));
        when(covid19ApiAdapter.getAllVaccines()).thenReturn(
                TestUtils.getTestDataFromJson("/vaccines-all.json", new TypeReference<>() {}));
        when(covid19ApiAdapter.getCases(any(CountryFilter.class))).thenCallRealMethod();
        when(covid19ApiAdapter.getVaccines(any(CountryFilter.class))).thenCallRealMethod();

        statsService = new AnalysisServiceImpl(covid19ApiAdapter);
    }

    @Test
    void calculateCorrelationCoefficientForAllCountries() {
        CountryFilter filter = new CountryFilter();
        VaccinesToDeathsPCC result = statsService.getVaccinesToDeathsPCC(filter);

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getCoefficient());
    }

    @Test
    void calculateCorrelationCoefficientForEurope() {
        CountryFilter filter = new CountryFilter();
        filter.setContinent("Europe");
        VaccinesToDeathsPCC result = statsService.getVaccinesToDeathsPCC(filter);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("-0.59") , result.getCoefficient());
    }
}
