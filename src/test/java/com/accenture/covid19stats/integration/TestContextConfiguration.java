package com.accenture.covid19stats.integration;

import com.accenture.covid19stats.Application;
import com.accenture.covid19stats.TestUtils;
import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.config.CachingConfiguration;
import com.accenture.covid19stats.config.FeignClientsConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcWebClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcWebDriverAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@EnableAutoConfiguration
@ImportAutoConfiguration({
        MockMvcAutoConfiguration.class,
        MockMvcWebClientAutoConfiguration.class,
        MockMvcWebDriverAutoConfiguration.class
})
@ComponentScan(
        basePackages = "com.accenture.covid19stats",
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Application.class),
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = FeignClientsConfiguration.class)
        }
)
public class TestContextConfiguration {

    @Bean
    public Covid19ApiClientV1 feignCovid19ApiClientV1()  {
        Covid19ApiClientV1 mock = mock(Covid19ApiClientV1.class);

        when(mock.getCases(null)).thenReturn(
                TestUtils.getTestDataFromJson("/cases-all.json", new TypeReference<>() {}));
        when(mock.getVaccines(null)).thenReturn(
                TestUtils.getTestDataFromJson("/vaccines-all.json", new TypeReference<>() {}));

        return mock;
    }
}
