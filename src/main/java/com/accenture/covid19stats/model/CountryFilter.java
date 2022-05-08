package com.accenture.covid19stats.model;

import com.accenture.covid19stats.api.model.CountryInfoDto;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CountryFilter implements Predicate<CountryInfoDto> {
    private String continent;
    @Singular("countryIso")
    private List<String> countryNames;

    @Override
    public boolean test(CountryInfoDto countryInfo) {
        if(!CollectionUtils.isEmpty(countryNames)) {
            return countryNames.contains(countryInfo.getCountry());
        } else if(StringUtils.isNoneBlank(continent)) {
            return continent.equals(countryInfo.getContinent());
        }

        return true;
    }
}
