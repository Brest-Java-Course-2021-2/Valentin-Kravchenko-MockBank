package com.epam.brest.model.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.validator.constraint.AnyPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Objects;

import static com.epam.brest.model.constant.ModelConstant.*;

public class AnyPatternValidator extends BasicValidator implements ConstraintValidator<AnyPattern, BankAccountFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(AnyPatternValidator.class);

    @Override
    public boolean isValid(BankAccountFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.isNull(value.getCustomerPattern()) && Objects.isNull(value.getNumberPattern())) {
            buildConstraint(context, CUSTOMER_PATTERN_TEMPLATE, CUSTOMER_PATTERN);
            buildConstraint(context, ACCOUNT_NUMBER_PATTERN_TEMPLATE, NUMBER_PATTERN);
            return false;
        }
        return true;
    }

}
