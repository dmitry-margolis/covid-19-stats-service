package com.accenture.covid19stats.access;

import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.model.CountryFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
