package com.zonesoft.persons.configurations;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.util.Date;

public class OffsetDateTimeToDate implements Converter<OffsetDateTime, Date> {
	
    @Override
    public Date convert(OffsetDateTime odt) {
        return Date.from(odt.toInstant());
    }
    
}