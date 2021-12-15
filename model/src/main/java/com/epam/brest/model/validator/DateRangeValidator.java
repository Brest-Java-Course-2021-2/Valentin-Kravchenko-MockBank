package com.epam.brest.model.validator;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.validator.constraint.DateRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static com.epam.brest.model.constant.ModelConstant.*;

public class DateRangeValidator extends BasicValidator implements ConstraintValidator<DateRange, CreditCardDateRangeDto> {

    private static final Logger LOGGER = LogManager.getLogger(DateRangeValidator.class);

    @Value("${card.filter.date.regexp}")
    private String dateRegexp;

    @Value("${card.filter.date.pattern}")
    private String datePattern;

    @Override
    public boolean isValid(CreditCardDateRangeDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        boolean isValueFromDateValid = true;
        boolean isValueToDateValid = true;
        if ((value.getValueFromDate().isEmpty() && value.getValueToDate().isEmpty())) {
            buildConstraint(context, DATE_RANGE_VALUE_FROM_DATE_TEMPLATE, VALUE_FROM_DATE);
            buildConstraint(context, DATE_RANGE_VALUE_TO_DATE_TEMPLATE, VALUE_TO_DATE);
            return false;
        }
        if (!value.getValueFromDate().isEmpty()) {
            if (!value.getValueFromDate().matches(dateRegexp)) {
                buildConstraint(context, DATE_RANGE_VALUE_FROM_DATE_TEMPLATE, VALUE_FROM_DATE);
                isValueFromDateValid = false;
            } else {
                YearMonth yearMonth = YearMonth.parse(value.getValueFromDate(), dateTimeFormatter);
                value.setFromDate(yearMonth.atEndOfMonth());
            }
        }
        if (!value.getValueToDate().isEmpty()) {
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
