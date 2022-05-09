package com.accenture.covid19stats.api.client;

import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
        qualifiers = "Covid19ApiClientV1Feign",
        name = "${client.covid19-api.name}-v1",
        url = "${client.covid19-api.url}",
        primary = false
)
public interface Covid19ApiClientV1 {

    @GetMapping("/cases")
    Map<String, CountryCasesDto> getCases(@RequestParam(name = "continent", required = false) String continent);

    @GetMapping("/vaccines")
    Map<String, CountryVaccinesDto> getVaccines(@RequestParam(name = "continent", required = false) String continent);
}
