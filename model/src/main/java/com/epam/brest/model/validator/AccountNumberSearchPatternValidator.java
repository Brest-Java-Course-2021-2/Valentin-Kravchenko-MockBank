package com.epam.brest.model.validator;

import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountNumberSearchPatternValidator implements ConstraintValidator<AccountNumberPattern, String> {

    private static final Logger LOGGER = LogManager.getLogger(AccountNumberSearchPatternValidator.class);

    @Value("${account.filter.number.regexp}")
    private String numberRegexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (value.isEmpty()) {
            return true;
        }
        return value.matches(numberRegexp);
    }

}
