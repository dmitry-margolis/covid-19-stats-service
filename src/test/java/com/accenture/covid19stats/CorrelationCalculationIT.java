package com.accenture.covid19stats;

import com.accenture.covid19stats.access.Covid19ApiAdapter;
import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import com.accenture.covid19stats.model.CountryFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Covid19statsApplication.class)
@AutoConfigureMockMvc
@Import(TestConfigurationMock.class)
//@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class CorrelationCalculationIT {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private Covid19ApiClientV1 covid19ApiClientV1;

    @Test
    void calculate() throws Exception {
        when(covid19ApiClientV1.getAllCases()).thenReturn(
                TestUtils.getTestDataFromJson("/cases-all.json", new TypeReference<>() {}));
        when(covid19ApiClientV1.getAllVaccines()).thenReturn(
                TestUtils.getTestDataFromJson("/vaccines-all.json", new TypeReference<>() {}));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/stats/correlation-coefficient?continent={continent}", "Europe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coefficient", is(-0.59)));

        verify(covid19ApiClientV1, times(1)).getAllCases();
        verify(covid19ApiClientV1, times(1)).getAllVaccines();

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/stats/correlation-coefficient?continent={continent}", "Europe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coefficient", is(-0.59)));

        verify(covid19ApiClientV1, times(1)).getAllCases();
        verify(covid19ApiClientV1, times(1)).getAllVaccines();
    }
}
