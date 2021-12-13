package com.epam.brest.model.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.validator.constraint.AnyPatterns;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnyPatternsValidator implements ConstraintValidator<AnyPatterns, BankAccountFilterDto> {

    private static final String CUSTOMER_PATTERN_TEMPLATE = "{CustomerPattern.account.dto.customerPattern}";
    private static final String PATTERN_ACCOUNT_NUMBER_TEMPLATE = "{AccountNumberPattern.account.dto.numberPattern}";

    @Override
    public boolean isValid(BankAccountFilterDto value, ConstraintValidatorContext context) {
        if (value.getCustomerPattern().isEmpty() && value.getNumberPattern().isEmpty()) {
            context.buildConstraintViolationWithTemplate(CUSTOMER_PATTERN_TEMPLATE).addConstraintViolation()
                   .buildConstraintViolationWithTemplate(PATTERN_ACCOUNT_NUMBER_TEMPLATE).addConstraintViolation();
            return false;
        }
        return true;
    }

}
