package com.epam.brest.model.validator;

import com.epam.brest.model.validator.constant.RangeDateType;
import com.epam.brest.model.validator.constraint.RangeDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.epam.brest.model.validator.constant.RangeDateType.END;
import static com.epam.brest.model.validator.constant.RangeDateType.START;
import static com.epam.brest.model.validator.constant.ValidatorConstant.RANGE_DATE_FROM_DATE_TEMPLATE;
import static com.epam.brest.model.validator.constant.ValidatorConstant.RANGE_DATE_TO_DATE_TEMPLATE;
import static java.util.Objects.isNull;

public class DateRangeValidator extends BasicValidator implements ConstraintValidator<RangeDate, String> {

    private static final Logger LOGGER = LogManager.getLogger(DateRangeValidator.class);

    @Value("${card.filter.date.regexp}")
    private String dateRegexp;

    private RangeDateType rangeDateType;

    @Override
    public void initialize(RangeDate constraintAnnotation) {
        rangeDateType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (isNull(value)) {
            return true;
        }
        if (!value.matches(dateRegexp)) {
            if (rangeDateType.equals(START)) {
                buildConstraintViolation(context, RANGE_DATE_FROM_DATE_TEMPLATE);
            } else if (rangeDateType.equals(END)) {
                buildConstraintViolation(context, RANGE_DATE_TO_DATE_TEMPLATE);
            }
            return false;
        }
        return true;
    }

}
