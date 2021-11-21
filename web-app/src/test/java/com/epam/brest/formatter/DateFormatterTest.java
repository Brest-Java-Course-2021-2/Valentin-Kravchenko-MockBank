package com.epam.brest.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-formatter.xml"})
class DateFormatterTest {

    private final DateFormatter dateFormatter;

    public DateFormatterTest(@Autowired DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Test
    void parse() {
        LocalDate localDate = dateFormatter.parse("19.11.2021", null);
        assertEquals(localDate, LocalDate.of(2021, 11, 19));
    }

    @Test
    void print() {
        LocalDate now = LocalDate.of(2021, 11, 19);
        String print = dateFormatter.print(now, null);
        assertEquals(print, "19.11.2021");
    }

}