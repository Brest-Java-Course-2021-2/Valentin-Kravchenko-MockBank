package com.epam.brest.model.validator;

import com.epam.brest.model.validator.constraint.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerValidator implements ConstraintValidator<Customer, String> {

    private static final Logger LOGGER = LogManager.getLogger(CustomerValidator.class);

    @Value("${account.customer.regexp}")
    private String customerRegexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        return value.matches(customerRegexp);
    }

}
