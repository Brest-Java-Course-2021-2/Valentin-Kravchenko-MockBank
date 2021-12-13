package com.epam.brest.model.validator;

import com.epam.brest.model.validator.constraint.CustomerPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerSearchPattenValidator implements ConstraintValidator<CustomerPattern, String> {

    private static final Logger LOGGER = LogManager.getLogger(CustomerSearchPattenValidator.class);

    @Value("${account.filter.customer.regexp}")
    private String customerRegexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (value.isEmpty()) {
            return true;
        }
        return value.matches(customerRegexp);
    }

}
