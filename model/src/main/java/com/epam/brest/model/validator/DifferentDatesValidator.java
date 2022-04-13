package com.epam.brest.model.validator;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.validator.constraint.DifferentDates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.validator.constant.ValidatorConstant.*;

public class DifferentDatesValidator extends BasicValidator implements ConstraintValidator<DifferentDates, CreditCardFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(DifferentDatesValidator.class);

    @Override
    public boolean isValid(CreditCardFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.nonNull(value.getFromDate()) && Objects.nonNull(value.getToDate()) && value.getFromDate().equals(value.getToDate())) {
            buildConstraintViolation(context, DIFFERENT_DATES_VALUE_FROM_DATE_TEMPLATE, FROM_DATE_VALUE);
            buildConstraintViolation(context, DIFFERENT_DATES_VALUE_TO_DATE_TEMPLATE, TO_DATE_VALUE);
            return false;
        }
        return true;
    }

}
