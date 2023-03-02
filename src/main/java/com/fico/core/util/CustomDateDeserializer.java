package com.fico.core.util;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.jcs3.access.exception.InvalidArgumentException;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<java.time.LocalDateTime> {

	private  int milliToNanoConst = 1000000;
    private  final String[] DATE_FORMATS = new String[] {
            "dd-MM-yyyy", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd", "yyyyMMdd'T'hh:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    };
    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public java.time.LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        String date = jsonparser.getText();

        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                final org.joda.time.LocalDateTime localDateTime =
                        org.joda.time.LocalDateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT));
                return java.time.LocalDateTime.of(
                        localDateTime.getYear(),
                        localDateTime.getMonthOfYear(),
                        localDateTime.getDayOfMonth(),
                        localDateTime.getHourOfDay(),
                        localDateTime.getMinuteOfHour(),
                        localDateTime.getSecondOfMinute(),
                        localDateTime.getMillisOfSecond() * milliToNanoConst );
            } catch (Exception e) {
                System.out.println(e);
            }
        }
            throw new InvalidArgumentException("date format not fall into allowed formats"
                                                            + Arrays.asList(DATE_FORMATS));
    }
}