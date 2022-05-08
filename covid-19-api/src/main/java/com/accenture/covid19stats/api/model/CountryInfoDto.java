package com.accenture.covid19stats.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CountryInfoDto implements Serializable {
    private String country;
    private long population;
    @JsonProperty("sq_km_area")
    private int sqKmArea;
    @JsonProperty("life_expectancy")
    private String lifeExpectancy;
    @JsonProperty("elevation_in_meters")
    private String elevationInMeters;
    private String continent;
    private String abbreviation;
    private String location;
    private int iso;
    @JsonProperty("capital_city")
    private String capitalCity;
}
