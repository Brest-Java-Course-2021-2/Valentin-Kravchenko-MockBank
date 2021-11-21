package com.epam.brest.formatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter implements Formatter<LocalDate> {

    @Value("${date.format}")
    private String dateFormat;

    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(dateFormat));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.format(DateTimeFormatter.ofPattern(dateFormat));
    }

}
