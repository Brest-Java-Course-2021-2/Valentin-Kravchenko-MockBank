package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        if (Objects.isNull(creditCardDateRangeDto.getValueFromDate()) &&
            Objects.isNull(creditCardDateRangeDto.getValueToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
        }
        if (Objects.nonNull(creditCardDateRangeDto.getValueFromDate())) {
            if (!creditCardDateRangeDto.getValueFromDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            }
        }
        if (Objects.nonNull(creditCardDateRangeDto.getValueToDate())) {
            if (!creditCardDateRangeDto.getValueToDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
            }
        }
        if (Objects.nonNull(creditCardDateRangeDto.getValueFromDate()) &&
            Objects.nonNull(creditCardDateRangeDto.getValueToDate()) &&
            creditCardDateRangeDto.getValueFromDate().equals(creditCardDateRangeDto.getValueToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_TO_DATE);
        }
    }

}
