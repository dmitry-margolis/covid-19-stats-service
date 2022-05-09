package com.accenture.covid19stats.integration;

import com.accenture.covid19stats.api.client.Covid19ApiClientV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestContextConfiguration.class)
class AnalysisControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("feignCovid19ApiClientV1")
    private Covid19ApiClientV1 covid19ApiClientV1;

    private static final String COUNTY_SUMMARIES_GET_API = "/api/v1/analysis/summary?continent={continent}";
    private static final String VACCINES_TO_DEATHS_PCC_GET_API = "/api/v1/analysis/vaccines-to-deaths-pcc?continent={continent}";

    @Test
    void Get_country_summaries() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(COUNTY_SUMMARIES_GET_API, "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(182)));

        mvc.perform(MockMvcRequestBuilders.get(COUNTY_SUMMARIES_GET_API, "Europe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(42)));
    }

    @Test
    void Get_vaccines_to_deaths_PCC() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(VACCINES_TO_DEATHS_PCC_GET_API, "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coefficient", is(-0.36)));

        verify(covid19ApiClientV1).getCases(null);
        verify(covid19ApiClientV1).getVaccines(null);

        mvc.perform(MockMvcRequestBuilders.get(VACCINES_TO_DEATHS_PCC_GET_API, "Europe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coefficient", is(-0.79)));

        verify(covid19ApiClientV1).getCases(null);
        verify(covid19ApiClientV1).getVaccines(null);
    }

    @Test
    void Get_vaccines_to_deaths_PCC_no_data() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(VACCINES_TO_DEATHS_PCC_GET_API, "Eastasia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.errorCode", is("NO_DATA_FOUND")));
    }
}
