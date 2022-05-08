package com.accenture.covid19stats.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
public class CountrySummary {
    private String country;
    private int iso;
    private Long population;
    private Long confirmed;
    private Long recovered;
    private Long deaths;
    private Long vaccinated;
    private Long partiallyVaccinated;

    private BigDecimal fatalityRatePercent;
    private BigDecimal vaccinationRatePercent;

    public void calculateRates(MathContext mathContext) {
        if(confirmed != 0) {
            fatalityRatePercent = BigDecimal.valueOf(deaths)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(confirmed), mathContext);
        }
        if(vaccinated != null && population != null && population > 0) {
            vaccinationRatePercent = BigDecimal.valueOf(vaccinated)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(population), mathContext);
        }
    }
}
