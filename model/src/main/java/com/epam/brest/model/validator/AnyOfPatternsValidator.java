package com.epam.brest.model.validator;

import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.model.validator.constraint.AnyOfPatterns;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.epam.brest.model.validator.constant.ValidatorConstant.*;
import static java.util.Objects.isNull;

public class AnyOfPatternsValidator extends BasicValidator implements ConstraintValidator<AnyOfPatterns, BankAccountFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(AnyOfPatternsValidator.class);

    @Override
    public boolean isValid(BankAccountFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (isNull(value.getCustomerPattern()) && isNull(value.getNumberPattern())) {
            buildConstraintViolation(context, CUSTOMER_PATTERN_TEMPLATE, CUSTOMER_PATTERN);
            buildConstraintViolation(context, ACCOUNT_NUMBER_PATTERN_TEMPLATE, NUMBER_PATTERN);
            return false;
        }
        return true;
    }

}
