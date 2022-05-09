package com.accenture.covid19stats.config;

import com.accenture.covid19stats.service.cache.CacheEventLogger;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class CachingConfiguration {

    public static final String COVID_API_CACHE = "covidApiCache";

    @Bean
    public CacheManager ehCacheManager(CovidApiProperties covidApiProperties) {
        CovidApiProperties.CachingProperties properties = covidApiProperties.getCaching();
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        CacheEventListenerConfigurationBuilder eventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.EXPIRED)
                .unordered().asynchronous();


        CacheConfigurationBuilder<String, Object> configuration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                Object.class,
                                ResourcePoolsBuilder.heap(properties.getHeapEntries()))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(properties.getTtlInMinutes())))
                        .withService(eventListenerConfiguration)
                        .withService(new DefaultSizeOfEngineConfiguration(10, MemoryUnit.MB, 10000));

        javax.cache.configuration.Configuration<String, Object> cacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(configuration);

        cacheManager.createCache(COVID_API_CACHE, cacheConfiguration);

        log.info("Configured covid api cache with ttl={}min, heap={}entries",
                properties.getTtlInMinutes(), properties.getHeapEntries());

        return cacheManager;
    }
}
