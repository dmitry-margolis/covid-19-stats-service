package com.accenture.covid19stats.unit;

import com.accenture.covid19stats.TestUtils;
import com.accenture.covid19stats.access.Covid19ApiAdapterImpl;
import com.accenture.covid19stats.access.Covid19ApiCacheDecoratorImpl;
import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.mapper.CountrySummaryMapperImpl;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.error.ErrorCode;
import com.accenture.covid19stats.model.error.ErrorCodeEnum;
import com.accenture.covid19stats.model.error.ServiceBusinessException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private Covid19ApiClientV1 covid19ApiClientV1;

    private AnalysisService analysisService;

    @BeforeEach
    void init() {
        when(covid19ApiClientV1.getCases(null)).thenReturn(
                TestUtils.getTestDataFromJson("/cases-all.json", new TypeReference<>() {}));
        when(covid19ApiClientV1.getVaccines(null)).thenReturn(
                TestUtils.getTestDataFromJson("/vaccines-all.json", new TypeReference<>() {}));

        analysisService = new AnalysisServiceImpl(
                new Covid19ApiAdapterImpl(
                    new Covid19ApiCacheDecoratorImpl(covid19ApiClientV1),
                    new CountrySummaryMapperImpl()
                )
            );
    }

    @Test
    void Get_country_summaries() {
        CountryFilter filter = new CountryFilter();
        List<CountrySummary> summaries = analysisService.getCountrySummaries(filter);

        assertNotNull(summaries);
        assertEquals(182, summaries.size());
        summaries.forEach(s -> {
            assertNotNull(s.getFatalityRatePercent());
            assertTrue(s.getFatalityRatePercent().compareTo(BigDecimal.ZERO) >=0
                    && s.getFatalityRatePercent().compareTo(BigDecimal.valueOf(100)) <= 0);
            if(s.getVaccinated() != null) {
                assertNotNull(s.getVaccinationRatePercent());
                assertTrue(s.getVaccinationRatePercent().compareTo(BigDecimal.ZERO) >=0
                        && s.getVaccinationRatePercent().compareTo(BigDecimal.valueOf(100)) <= 0, s.getCountry());
            } else {
                assertNull(s.getVaccinationRatePercent());
            }
        });
    }

    @Test
    void Get_vaccines_to_deaths_PCC_for_all_countries() {
        CountryFilter filter = new CountryFilter();
        VaccinesToDeathsPCC result = analysisService.getVaccinesToDeathsPCC(filter);

        assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(-0.36), result.getCoefficient());
    }

    @Test
    void Get_vaccines_to_deaths_PCC_for_europe() {
        final CountryFilter filter = new CountryFilter();
        filter.setContinent("Europe");
        VaccinesToDeathsPCC result = analysisService.getVaccinesToDeathsPCC(filter);

        assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(-0.79) , result.getCoefficient());
    }

    @Test
    void Get_vaccines_to_deaths_PCC_for_no_data() {
        final CountryFilter filter = new CountryFilter();
        filter.setContinent("Eastasia");

        ServiceBusinessException ex = assertThrows(ServiceBusinessException.class, () -> analysisService.getVaccinesToDeathsPCC(filter));
        assertEquals(ErrorCodeEnum.NO_DATA_FOUND, ex.getCode());
    }
}
