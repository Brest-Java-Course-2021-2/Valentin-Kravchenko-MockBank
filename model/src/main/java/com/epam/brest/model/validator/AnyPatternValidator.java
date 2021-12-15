package com.epam.brest.model.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.validator.constraint.AnyPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.epam.brest.model.constant.ModelConstant.*;

public class AnyPatternValidator extends BasicValidator implements ConstraintValidator<AnyPattern, BankAccountFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(AnyPatternValidator.class);

    @Override
    public boolean isValid(BankAccountFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (value.getCustomerPattern().isEmpty() && value.getNumberPattern().isEmpty()) {
            HibernateConstraintValidatorContext validatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
            buildConstraint(validatorContext, CUSTOMER_PATTERN_TEMPLATE, CUSTOMER_PATTERN);
            buildConstraint(validatorContext, ACCOUNT_NUMBER_PATTERN_TEMPLATE, NUMBER_PATTERN);
            return false;
        }
        return true;
    }

}
