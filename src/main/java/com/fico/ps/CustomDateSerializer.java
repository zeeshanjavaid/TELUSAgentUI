package com.fico.ps;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class CustomDateSerializer extends JsonDeserializer<ZonedDateTime> {

    private static final Logger logger = LoggerFactory.getLogger(CustomDateSerializer.class);
    private static final long serialVersionUID = 1L;

    private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX"))
            .appendOptional(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX"))
            .toFormatter();

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        return ZonedDateTime.parse(p.getText(), formatter);
    }
}