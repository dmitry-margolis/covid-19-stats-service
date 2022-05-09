package com.accenture.covid19stats.access;

import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.config.CachingConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Decorator for caching data from Covid-19 API
 */
@Service
@RequiredArgsConstructor
@Primary
public class Covid19ApiCacheDecoratorImpl implements Covid19ApiClientV1 {

    private final Covid19ApiClientV1 covid19ApiClientV1;

    @Override
    @Cacheable(cacheNames = CachingConfiguration.COVID_API_CACHE,
            key = "#root.method.name + #continent", sync = true)
    public Map<String, CountryCasesDto> getCases(String continent) {
        return covid19ApiClientV1.getCases(continent)
                .entrySet().stream()
                .filter(e -> e.getValue().getTotal().getCountry() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    @Cacheable(cacheNames = CachingConfiguration.COVID_API_CACHE,
            key = "#root.method.name + #continent", sync = true)
    public Map<String, CountryVaccinesDto> getVaccines(String continent) {
        return covid19ApiClientV1.getVaccines(continent)
                .entrySet().stream()
                .filter(e -> e.getValue().getTotal().getCountry() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
