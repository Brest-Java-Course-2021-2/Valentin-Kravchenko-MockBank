package com.epam.brest.model.validator;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.validator.constraint.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.epam.brest.model.validator.constant.ValidatorConstant.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class RangeValidator extends BasicValidator implements ConstraintValidator<Range, CreditCardFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(RangeValidator.class);

    @Override
    public boolean isValid(CreditCardFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if ((isNull(value.getFromDateValue()) && isNull(value.getToDateValue()))
            ||
            (nonNull(value.getFromDateValue()) && nonNull(value.getToDateValue()) &&
             value.getFromDateValue().equals(value.getToDateValue()))) {
            buildConstraintViolation(context, RANGE_FROM_DATE_TEMPLATE, FROM_DATE_VALUE);
            buildConstraintViolation(context, RANGE_TO_DATE_TEMPLATE, TO_DATE_VALUE);
            return false;
        }
        return true;
    }

}
