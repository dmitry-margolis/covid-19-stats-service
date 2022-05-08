package com.accenture.covid19stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;

@UtilityClass
public class TestUtils {
    @SneakyThrows
    public <T> T getTestDataFromJson(String path, TypeReference<T> typeReference) {
        InputStream stream = TestUtils.class.getResourceAsStream(path);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(stream, typeReference);
    }
}
