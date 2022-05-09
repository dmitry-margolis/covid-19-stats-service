package com.accenture.covid19stats.config;

import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.accenture.covid19stats.api.client"})
@Slf4j
public class FeignClientsConfiguration {

    @Bean
    public Retryer retryer(CovidApiProperties covidApiProperties) {
        CovidApiProperties.RetryProperties retryProperties = covidApiProperties.getRetry();
        log.info("Configured covid api retry with maxAttempts={}, period={}->{}ms",
                retryProperties.getMaxAttempts(), retryProperties.getPeriodInMillis(), retryProperties.getMaxPeriodInMillis());
        return new Retryer.Default(
                retryProperties.getPeriodInMillis(),
                retryProperties.getMaxPeriodInMillis(),
                retryProperties.getMaxAttempts());
    }
}