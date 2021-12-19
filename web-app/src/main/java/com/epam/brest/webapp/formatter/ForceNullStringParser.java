package com.epam.brest.webapp.formatter;

import org.springframework.format.Parser;

import java.util.Locale;

public class ForceNullStringParser implements Parser<String> {

    @Override
    public String parse(String text, Locale locale) {
        return text.isEmpty() ? null : text;
    }

}
