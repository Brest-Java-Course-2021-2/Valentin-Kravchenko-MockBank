package com.epam.brest.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public final class ControllerUtils {

    private ControllerUtils() {
    }

    public static String extractErrorFields(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                            .stream()
                            .map(FieldError::getField)
                            .collect(Collectors.joining(", ", "[", "]"));

    }

}
