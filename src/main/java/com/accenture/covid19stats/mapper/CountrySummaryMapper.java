package com.accenture.covid19stats.mapper;

import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.model.CountrySummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountrySummaryMapper {
    @Mapping(target = "country", source = "total.country")
    @Mapping(target = "iso", source = "total.iso")
    @Mapping(target = "population", source = "total.population")
    @Mapping(target = "confirmed", source = "total.confirmed")
    @Mapping(target = "deaths", source = "total.deaths")
    @Mapping(target = "recovered", source = "total.recovered")
    CountrySummary summaryFromCountryCasesDto(CountryCasesDto dto);
}