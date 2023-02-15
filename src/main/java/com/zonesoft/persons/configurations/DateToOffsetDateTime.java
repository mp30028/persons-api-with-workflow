package com.zonesoft.persons.configurations;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateToOffsetDateTime implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}
