package com.accenture.covid19stats.service;

import com.accenture.covid19stats.access.Covid19ApiAdapter;
import com.accenture.covid19stats.model.VaccinesToDeathsPCC;
import com.accenture.covid19stats.model.CountryFilter;
import com.accenture.covid19stats.model.CountrySummary;
import com.accenture.covid19stats.model.error.ServiceBusinessException;
import com.accenture.covid19stats.model.error.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    private final Covid19ApiAdapter covid19ApiAdapter;

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

    @Override
    public List<CountrySummary> getCountrySummaries(CountryFilter filter) {
        return covid19ApiAdapter.getCountriesSummary(filter)
                .stream().map(summary -> summary.calculateRates(MATH_CONTEXT))
                .collect(Collectors.toList());
    }

    @Override
    public VaccinesToDeathsPCC getVaccinesToDeathsPCC(CountryFilter filter) {
        List<CountrySummary> countrySummaries = getCountrySummaries(filter).stream()
                .filter(s -> s.getFatalityRatePercent() != null && s.getVaccinationRatePercent() != null)
                .collect(Collectors.toList());

        if(countrySummaries.isEmpty()) {
            throw new ServiceBusinessException(ErrorCodeEnum.NO_DATA_FOUND, filter);
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
