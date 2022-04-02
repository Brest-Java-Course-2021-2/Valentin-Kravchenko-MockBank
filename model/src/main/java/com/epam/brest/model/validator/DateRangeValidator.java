package com.epam.brest.model.validator;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.validator.constraint.DateRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.epam.brest.model.validator.constant.ValidatorConstant.*;

public class DateRangeValidator extends BasicValidator implements ConstraintValidator<DateRange, CreditCardFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(DateRangeValidator.class);

    @Value("${card.filter.date.regexp}")
    private String dateRegexp;

    @Value("${card.filter.date.pattern}")
    private String datePattern;

    @Override
    public boolean isValid(CreditCardFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        boolean isValueFromDateValid = true;
        boolean isValueToDateValid = true;
        if ((Objects.isNull(value.getValueFromDate()) && Objects.isNull(value.getValueToDate()))) {
            buildConstraint(context, DATE_RANGE_VALUE_FROM_DATE_TEMPLATE, VALUE_FROM_DATE);
            buildConstraint(context, DATE_RANGE_VALUE_TO_DATE_TEMPLATE, VALUE_TO_DATE);
            return false;
        }
        if (Objects.nonNull(value.getValueFromDate())) {
            if (!value.getValueFromDate().matches(dateRegexp)) {
                buildConstraint(context, DATE_RANGE_VALUE_FROM_DATE_TEMPLATE, VALUE_FROM_DATE);
                isValueFromDateValid = false;
            } else {
                YearMonth yearMonth = YearMonth.parse(value.getValueFromDate(), dateTimeFormatter);
                value.setFromDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(value.getValueToDate())) {
            if (!value.getValueToDate().matches(dateRegexp)) {
                buildConstraint(context, DATE_RANGE_VALUE_TO_DATE_TEMPLATE, VALUE_TO_DATE);
                isValueToDateValid = false;
            } else {
                YearMonth yearMonth = YearMonth.parse(value.getValueToDate(), dateTimeFormatter);
                value.setToDate(yearMonth.atEndOfMonth());
            }
        }
        return isValueFromDateValid && isValueToDateValid;
    }

}
