package com.accenture.covid19stats.api.model.vaccines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryVaccinesDto implements Serializable {
    @JsonProperty("All")
    private CountryVaccinesTotalDto total;
}
