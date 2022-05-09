package com.accenture.covid19stats.access;

import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.mapper.CountrySummaryMapper;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.model.error.ErrorCodeEnum;
import com.accenture.covid19stats.model.error.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Covid19ApiAdapterImpl implements Covid19ApiAdapter {

    private final Covid19ApiClientV1 covid19ApiClient;
    private final CountrySummaryMapper countrySummaryMapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
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
        Future<Map<String, CountryCasesDto>> casesByCountryMapFuture = executorService.submit(() -> getCases(filter));
        Future<Map<String, CountryVaccinesDto>> vaccinationByCountryMapFuture = executorService.submit(() -> getVaccines(filter));

        try {
            Map<String, CountryCasesDto> casesByCountryMap = casesByCountryMapFuture.get();
            Map<String, CountryVaccinesDto> vaccinationByCountryMap = vaccinationByCountryMapFuture.get();
            return casesByCountryMap.values().stream()
                    .map(cases -> {
                        CountrySummary summary = countrySummaryMapper.summaryFromCountryCasesDto(cases);
                        CountryVaccinesDto vaccinesData = vaccinationByCountryMap.get(summary.getCountry());
                        if(vaccinesData != null) {
                            summary.setVaccinated(vaccinesData.getTotal().getVaccinated());
                            summary.setPartiallyVaccinated(vaccinesData.getTotal().getPartiallyVaccinated());
                        }

                        return summary;
                    }).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceBusinessException(ErrorCodeEnum.FETCHING_COVID19_API_ERROR, e);
        }
    }

    private Map<String, CountryCasesDto> getAllCases() {
        return covid19ApiClient.getCases(null);
    }

    private Map<String, CountryVaccinesDto> getAllVaccines() {
        return covid19ApiClient.getVaccines(null);
    }
}
