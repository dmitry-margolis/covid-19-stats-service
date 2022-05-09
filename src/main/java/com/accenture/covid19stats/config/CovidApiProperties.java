package com.accenture.covid19stats.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Positive;

@Component
@Getter@ConfigurationProperties(prefix = "client.covid19-api")
@Setter
public class CovidApiProperties {
    private CachingProperties caching;
    private RetryProperties retry;

    @Data
    static class CachingProperties {
        @Positive
        int ttlInMinutes = 60;
        @Positive
        int heapEntries = 4;
    }

    @Data
    static class RetryProperties {
        @Positive
        Integer maxAttempts = 3;
        @Positive
        Long periodInMillis = 500L;
        @Positive
        Long maxPeriodInMillis = 3000L;
    }
}
