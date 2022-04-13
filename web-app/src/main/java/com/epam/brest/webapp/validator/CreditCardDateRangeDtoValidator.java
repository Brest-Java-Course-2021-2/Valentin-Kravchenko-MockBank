package com.epam.brest.webapp.validator;

import com.epam.brest.model.CreditCardFilterDto;
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
        return CreditCardFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        CreditCardFilterDto creditCardFilterDto = (CreditCardFilterDto) target;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        if (Objects.isNull(creditCardFilterDto.getFromDateValue()) &&
            Objects.isNull(creditCardFilterDto.getToDateValue())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
        }
        if (Objects.nonNull(creditCardFilterDto.getFromDateValue())) {
            if (!creditCardFilterDto.getFromDateValue().matches(dateRegexp)) {
                errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            }else {
                YearMonth yearMonth = YearMonth.parse(creditCardFilterDto.getFromDateValue(), dateTimeFormatter);
                creditCardFilterDto.setFromDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(creditCardFilterDto.getToDateValue())) {
            if (!creditCardFilterDto.getToDateValue().matches(dateRegexp)) {
                errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
            }else {
                YearMonth yearMonth = YearMonth.parse(creditCardFilterDto.getToDateValue(), dateTimeFormatter);
                creditCardFilterDto.setToDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(creditCardFilterDto.getFromDate()) &&
            Objects.nonNull(creditCardFilterDto.getToDate()) &&
            creditCardFilterDto.getFromDate().equals(creditCardFilterDto.getToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_TO_DATE);
        }
    }

}
