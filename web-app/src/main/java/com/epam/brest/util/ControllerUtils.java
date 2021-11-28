package com.epam.brest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

import static com.epam.brest.constant.ControllerConstant.*;

public final class ControllerUtils {

    private static final Logger LOGGER = LogManager.getLogger(ControllerUtils.class);

    private ControllerUtils() {
    }

    public static String extractErrorFields(BindingResult bindingResult) {
        LOGGER.trace("extractErrorFields()");
        return bindingResult.getFieldErrors()
                            .stream()
                            .map(FieldError::getField)
                            .collect(Collectors.joining(JOIN_DELIMITER, JOIN_PREFIX, JOIN_SUFFIX));

    }

}
