package com.epam.brest.formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.Formatter;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalPrinter implements Printer<BigDecimal> {

    private static final Logger LOGGER = LogManager.getLogger(BigDecimalPrinter.class);

    @Value("${big.decimal.pattern}")
    private String bigDecimalPattern;

    @Override
    public String print(BigDecimal object, Locale locale) {
        LOGGER.info("print(bigDecimal={}, locale={})", object, locale);
        DecimalFormat decimalFormat = new DecimalFormat(bigDecimalPattern, DecimalFormatSymbols.getInstance(locale));
        return decimalFormat.format(object);
    }

}
