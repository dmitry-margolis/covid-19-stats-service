package com.accenture.covid19stats.access;

import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Covid19ApiAdapterImpl implements Covid19ApiAdapter {

    private final Covid19ApiClientV1 covid19ApiClient;

    @Override
    public Map<String, CountryCasesDto> getAllCases() {
        return covid19ApiClient.getCases(null);
    }

    @Override
    public Map<String, CountryVaccinesDto> getAllVaccines() {
        return covid19ApiClient.getVaccines(null);
    }

    @Override
    public Map<String, CountryCasesDto> getCases(CountryFilter filter) {
        return getAllCases().entrySet().stream()
                .filter(e -> filter.test(e.getValue().getTotal()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, CountryVaccinesDto> getVaccines(CountryFilter filter) {
        return getAllVaccines().entrySet().stream()
                .filter(e -> filter.test(e.getValue().getTotal()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<CountrySummary> getCountriesSummary(CountryFilter filter) {
        Map<String, CountryCasesDto> casesByCountryMap = getCases(filter);
        Map<String, CountryVaccinesDto> vaccinationByCountryMap = getVaccines(filter);

        return casesByCountryMap.values().stream()
                .map(cases -> {
                    CountrySummary summary = new CountrySummary();
                    summary.setCountry(cases.getTotal().getCountry());
                    summary.setIso(cases.getTotal().getIso());
                    summary.setPopulation(cases.getTotal().getPopulation());
                    summary.setConfirmed(cases.getTotal().getConfirmed());
                    summary.setDeaths(cases.getTotal().getDeaths());
                    summary.setRecovered(cases.getTotal().getRecovered());

                    CountryVaccinesDto vaccinesData = vaccinationByCountryMap.get(summary.getCountry());
                    if(vaccinesData != null) {
                        summary.setVaccinated(vaccinesData.getTotal().getVaccinated());
                        summary.setPartiallyVaccinated(vaccinesData.getTotal().getPartiallyVaccinated());
                    }

                    return summary;
                }).collect(Collectors.toList());
    }
}
