package com.accenture.covid19stats.service;

import com.accenture.covid19stats.access.Covid19ApiAdapter;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.model.error.ServiceBusinessException;
import com.accenture.covid19stats.model.error.ServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    private final Covid19ApiAdapter covid19ApiAdapter;

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

    @Override
    public List<CountrySummary> getCountrySummaries(CountryFilter filter) {
        Map<String, CountryCasesDto> casesByCountryMap = covid19ApiAdapter.getCases(filter);
        Map<String, CountryVaccinesDto> vaccinationByCountryMap = covid19ApiAdapter.getVaccines(filter);

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
                    summary.calculateRates(MATH_CONTEXT);

                    return summary;
                }).collect(Collectors.toList());
    }

    @Override
    public VaccinesToDeathsPCC getVaccinesToDeathsPCC(CountryFilter filter) {
        List<CountrySummary> countrySummaries = getCountrySummaries(filter).stream()
                .filter(s -> s.getFatalityRatePercent() != null && s.getVaccinationRatePercent() != null)
                .collect(Collectors.toList());

        if(countrySummaries.isEmpty()) {
            throw new ServiceBusinessException(ServiceErrorCode.NO_DATA_FOUND, filter);
        }

        double[] x = new double[countrySummaries.size()];
        double[] y = new double[countrySummaries.size()];

        for(int index = 0; index < countrySummaries.size(); index++) {
            CountrySummary summary = countrySummaries.get(index);
            x[index] = summary.getVaccinationRatePercent().doubleValue();
            y[index] = summary.getFatalityRatePercent().doubleValue();
        }

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(x, y);

        VaccinesToDeathsPCC result = new VaccinesToDeathsPCC();
        if(!Double.isNaN(correlation)) {
            result.setCoefficient(BigDecimal.valueOf(correlation).round(MATH_CONTEXT));
        }

        return result;
    }
}
