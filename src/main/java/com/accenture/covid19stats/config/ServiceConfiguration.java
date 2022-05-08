package com.accenture.covid19stats.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.accenture.covid19stats.api.client"})
public class ServiceConfiguration {
}