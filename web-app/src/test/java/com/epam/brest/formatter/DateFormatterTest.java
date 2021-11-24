package com.epam.brest.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-formatter.xml"})
class DateFormatterTest {

    private final Formatter<LocalDate> formatter;

    private static final String CUSTOM_DELIMITER = "/";

    public DateFormatterTest(@Autowired Formatter<LocalDate> formatter) {
        this.formatter = formatter;
    }

    @Test
    void parse() throws ParseException {
        LocalDate now = LocalDate.now();
        LocalDate parse = formatter.parse(getLocalDateValue(now), Locale.getDefault());
        assertEquals(parse, now);
    }

    @Test
    void print() {
        LocalDate now = LocalDate.now();
        String print = formatter.print(now, Locale.getDefault());
        assertEquals(print, getLocalDateValue(now));
    }

    private String getLocalDateValue(LocalDate now) {
        return now.getDayOfMonth() + CUSTOM_DELIMITER + now.getMonthValue() + CUSTOM_DELIMITER + now.getYear();
    }

}