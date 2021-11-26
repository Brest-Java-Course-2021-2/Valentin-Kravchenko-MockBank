package com.epam.brest.formatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Value("${date.format}")
    private String dateFormat;

    @Override
    public LocalDate parse(String text, Locale locale) throws DateTimeException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(dateFormat, locale));
    }

    @Override
    public String print(LocalDate object, Locale locale) throws DateTimeException {
        return object.format(DateTimeFormatter.ofPattern(dateFormat, locale));
    }

}
