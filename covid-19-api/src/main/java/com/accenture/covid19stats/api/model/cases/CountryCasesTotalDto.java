package com.accenture.covid19stats.api.model.cases;

import com.accenture.covid19stats.api.model.CountryInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class CountryCasesTotalDto extends CountryInfoDto implements Serializable {
    private long confirmed;
    private long recovered;
    private long deaths;

    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("long")
    private String longitude;
    private String updated;
}
