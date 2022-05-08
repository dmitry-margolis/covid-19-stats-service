package com.accenture.covid19stats.api.model;

import com.accenture.covid19stats.api.jackson.LocalDateKeyDeserializer;
import com.accenture.covid19stats.api.model.cases.CountryCasesDto;
import com.accenture.covid19stats.api.model.history.CountryHistoryCasesDto;
import com.accenture.covid19stats.api.model.vaccines.CountryVaccinesDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiModelTest {

    private ObjectMapper objectMapper;

    @BeforeAll
    void  initObjectMapper() {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
        objectMapper.registerModule(simpleModule);
    }

    @Test
    void testCasesModel() {
        try {
            Map<String, CountryCasesDto> data = objectMapper.readValue(
                    getClass().getResourceAsStream("/cases.json"),
                    new TypeReference<>() {});
            assertNotNull(data);
        } catch (IOException e) {
            fail("Exception " + e);
        }
    }

    @Test
    void testVaccinesModel() {
        try {
            Map<String, CountryVaccinesDto> data = objectMapper.readValue(
                    getClass().getResourceAsStream("/vaccines.json"),
                    new TypeReference<>() {});
            assertNotNull(data);
        } catch (IOException e) {
            fail("Exception " + e);
        }
    }
}
