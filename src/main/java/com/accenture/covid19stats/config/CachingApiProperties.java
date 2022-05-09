package com.accenture.covid19stats.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Positive;

/**
 * Properties for covid-19 data caching configuration
 */

@Component
@ConfigurationProperties(prefix = "client.covid-19-api.caching")
@Getter
@Setter
public class CachingApiProperties {
    @Positive
    int ttlInMinutes;
    @Positive
    int heapSizeInMBytes;
}
