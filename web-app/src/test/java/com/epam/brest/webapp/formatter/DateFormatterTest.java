package com.epam.brest.webapp.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-formatter.xml"})
class DateFormatterTest {

    private final Formatter<LocalDate> formatter;

    public DateFormatterTest(@Autowired Formatter<LocalDate> formatter) {
        this.formatter = formatter;
    }

    @Test
    void parse() throws ParseException {
        LocalDate now = LocalDate.now();
        LocalDate parse = formatter.parse(getLocalDateValue(now), new Locale("ru"));
        assertEquals(parse, now);
    }

    @Test
    void print() {
        LocalDate now = LocalDate.now();
        String print = formatter.print(now, new Locale("ru"));
        assertEquals(print, getLocalDateValue(now));
    }

    private String getLocalDateValue(LocalDate now) {
        return  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(now);
    }

}