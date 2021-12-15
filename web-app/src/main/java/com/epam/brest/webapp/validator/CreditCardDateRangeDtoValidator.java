package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Component
public class CreditCardDateRangeDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDateRangeDtoValidator.class);

    @Value("${card.filter.date.regexp}")
    private String dateRegexp;

    @Value("${card.filter.date.pattern}")
    private String datePattern;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreditCardDateRangeDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        CreditCardDateRangeDto creditCardDateRangeDto = (CreditCardDateRangeDto) target;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        if ((creditCardDateRangeDto.getValueFromDate().isEmpty() && creditCardDateRangeDto.getValueToDate().isEmpty())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
        }
        if (!creditCardDateRangeDto.getValueFromDate().isEmpty()) {
            if (!creditCardDateRangeDto.getValueFromDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            } else {
                YearMonth yearMonth = YearMonth.parse(creditCardDateRangeDto.getValueFromDate(), dateTimeFormatter);
                creditCardDateRangeDto.setFromDate(yearMonth.atEndOfMonth());
            }
        }
        if (!creditCardDateRangeDto.getValueToDate().isEmpty()) {
            if (!creditCardDateRangeDto.getValueToDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
            } else {
                YearMonth yearMonth = YearMonth.parse(creditCardDateRangeDto.getValueToDate(), dateTimeFormatter);
                creditCardDateRangeDto.setToDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(creditCardDateRangeDto.getFromDate()) &&
            Objects.nonNull(creditCardDateRangeDto.getToDate()) &&
            creditCardDateRangeDto.getFromDate().equals(creditCardDateRangeDto.getToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_TO_DATE);
        }
    }

}
