package com.epam.brest.model.validator;

import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AccountNumberPatternValidator implements ConstraintValidator<AccountNumberPattern, String> {

    private static final Logger LOGGER = LogManager.getLogger(AccountNumberPatternValidator.class);

    @Value("${account.filter.number.regexp}")
    private String numberRegexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.isNull(value)) {
            return true;
        }
        return value.matches(numberRegexp);
    }

}
