package com.accenture.covid19stats.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
@Schema(description = "Summary statistics for covid-19 cases and vaccination for selected countries")
public class CountrySummary {
    @Schema(description = "Country name")
    private String country;
    @Schema(description = "Country Iso code")
    private int iso;
    @Schema(description = "Country population")
    private Long population;
    @Schema(description = "Country confirmed covid-19 cases count")
    private Long confirmed;
    @Schema(description = "Country recovered from covid-19 cases count")
    private Long recovered;
    @Schema(description = "Country deaths from covid-19 cases count")
    private Long deaths;
    @Schema(description = "Country vaccinated people count")
    private Long vaccinated;
    @Schema(description = "Country partially vaccinated people count")
    private Long partiallyVaccinated;

    @Schema(description = "Country fatality rate percent")
    private BigDecimal fatalityRatePercent;
    @Schema(description = "Country vaccination rate percent")
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
