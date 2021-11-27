package com.epam.brest.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Printer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-formatter.xml"})
class BigDecimalPrinterTest {

    public static final String TEST_VALUE = "1200.23";
    public static final String RU_VALUE = "1\u00A0200,23";
    public static final String RU = "ru";

    private final Printer<BigDecimal> formatter;

    BigDecimalPrinterTest(@Autowired Printer<BigDecimal> formatter) {
        this.formatter = formatter;
    }

    @Test
    void print() {
        String formattedValue = formatter.print(new BigDecimal(TEST_VALUE), new Locale(RU));
        assertEquals(formattedValue, RU_VALUE);
    }
    
}