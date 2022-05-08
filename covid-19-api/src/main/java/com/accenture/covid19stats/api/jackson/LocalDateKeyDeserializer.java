package com.accenture.covid19stats.api.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.LocalDate;

public class LocalDateKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(final String key, final DeserializationContext ctxt) {
        return LocalDate.parse(key);
    }
}


