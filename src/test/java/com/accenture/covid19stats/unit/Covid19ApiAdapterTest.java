package com.accenture.covid19stats.unit;

import com.accenture.covid19stats.TestUtils;
import com.accenture.covid19stats.access.Covid19ApiAdapter;
import com.accenture.covid19stats.access.Covid19ApiAdapterImpl;
import com.accenture.covid19stats.access.Covid19ApiCacheDecoratorImpl;
import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.cases.CountryCasesTotalDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.mapper.CountrySummaryMapperImpl;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Covid19ApiAdapterTest {

    @Mock
    private Covid19ApiClientV1 covid19ApiClientV1;

    private Covid19ApiAdapter adapter;

    @BeforeEach
    void init() {
        lenient().when(covid19ApiClientV1.getCases(null)).thenReturn(
                TestUtils.getTestDataFromJson("/cases-all.json", new TypeReference<>() {}));
        lenient().when(covid19ApiClientV1.getVaccines(null)).thenReturn(
                TestUtils.getTestDataFromJson("/vaccines-all.json", new TypeReference<>() {}));

        adapter = new Covid19ApiAdapterImpl(
                    new Covid19ApiCacheDecoratorImpl(covid19ApiClientV1),
                    new CountrySummaryMapperImpl());
    }

    @Test
    void Get_all_cases() {
        Map<String, CountryCasesDto> cases = adapter.getCases(new CountryFilter());

        assertNotNull(cases);
        assertEquals(182, cases.size());

        verify(covid19ApiClientV1).getCases(null);
    }

    @Test
    void Get_continent_cases() {
        CountryFilter filter = CountryFilter.builder().continent("Europe").build();
        Map<String, CountryCasesDto> cases = adapter.getCases(filter);

        assertNotNull(cases);
        assertEquals(42, cases.size());
        cases.values().forEach(c -> assertEquals(filter.getContinent(), c.getTotal().getContinent()));

        verify(covid19ApiClientV1).getCases(null);
    }

    @Test
    void Get_unknown_continent_cases() {
        CountryFilter filter = CountryFilter.builder().continent("Eastasia").build();
        Map<String, CountryCasesDto> cases = adapter.getCases(filter);

        assertNotNull(cases);
        assertTrue(cases.isEmpty());
        verify(covid19ApiClientV1).getCases(null);
    }

    @Test
    void Get_all_vaccines() {
        Map<String, CountryVaccinesDto> vaccines = adapter.getVaccines(new CountryFilter());

        assertNotNull(vaccines);
        assertEquals(174, vaccines.size());

        verify(covid19ApiClientV1).getVaccines(null);
    }

    @Test
    void Get_continent_vaccines() {
        CountryFilter filter = CountryFilter.builder().continent("Europe").build();
        Map<String, CountryVaccinesDto> vaccines = adapter.getVaccines(filter);

        assertNotNull(vaccines);
        assertEquals(40, vaccines.size());
        vaccines.values().forEach(c -> assertEquals(filter.getContinent(), c.getTotal().getContinent()));

        verify(covid19ApiClientV1).getVaccines(null);
    }

    @Test
    void Get_unknown_continent_vaccines() {
        CountryFilter filter = CountryFilter.builder().continent("Eastasia").build();
        Map<String, CountryVaccinesDto> vaccines = adapter.getVaccines(filter);

        assertNotNull(vaccines);
        assertTrue(vaccines.isEmpty());
        verify(covid19ApiClientV1).getVaccines(null);
    }

    @Test
    void Get_countries_summary() {
        CountryFilter filter = new CountryFilter();
        List<CountrySummary> summaries = adapter.getCountriesSummary(filter);

        assertNotNull(summaries);
        assertEquals(182, summaries.size());

        verify(covid19ApiClientV1).getVaccines(null);
        verify(covid19ApiClientV1).getCases(null);

        Map<String, CountryVaccinesDto> vaccines = adapter.getVaccines(filter);
        Map<String, CountryCasesDto> cases = adapter.getCases(filter);
        summaries.forEach(summary -> {
            CountryCasesTotalDto countryCases = cases.get(summary.getCountry()).getTotal();
            CountryVaccinesDto countryVaccines = vaccines.get(summary.getCountry());
            assertEquals(countryCases.getConfirmed(), summary.getConfirmed());
            assertEquals(countryCases.getDeaths(), summary.getDeaths());
            assertEquals(countryCases.getPopulation(), summary.getPopulation());

            if(countryVaccines != null) {
                assertEquals(countryVaccines.getTotal().getVaccinated(), summary.getVaccinated());
                assertEquals(countryVaccines.getTotal().getPartiallyVaccinated(), summary.getPartiallyVaccinated());
            } else {
                assertNull(summary.getVaccinated());
                assertNull(summary.getPartiallyVaccinated());
            }
        });
    }
}
