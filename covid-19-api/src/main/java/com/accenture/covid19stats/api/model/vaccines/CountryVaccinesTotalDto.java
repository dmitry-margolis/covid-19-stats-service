package com.accenture.covid19stats.api.model.vaccines;

import com.accenture.covid19stats.api.model.CountryInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CountryVaccinesTotalDto extends CountryInfoDto implements Serializable {
    private long administered;
    @JsonProperty("people_vaccinated")
    private long vaccinated;
    @JsonProperty("people_partially_vaccinated")
    private long partiallyVaccinated;
    private String updated;
}
