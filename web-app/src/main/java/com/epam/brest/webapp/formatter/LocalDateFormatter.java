package com.epam.brest.webapp.formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    private static final Logger LOGGER = LogManager.getLogger(LocalDateFormatter.class);

    @Value("${date.format}")
    private String dateFormat;

    @Override
    public LocalDate parse(String text, Locale locale) {
        LOGGER.trace("parse(text={}, locale={})", text, locale);
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(dateFormat, locale));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        LOGGER.trace("print(localDate={}, locale={})", object, locale);
        return object.format(DateTimeFormatter.ofPattern(dateFormat, locale));
    }

}
