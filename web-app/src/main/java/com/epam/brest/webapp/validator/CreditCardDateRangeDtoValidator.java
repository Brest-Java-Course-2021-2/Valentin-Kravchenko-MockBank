package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.CreditCardsFilterDto;
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
        return CreditCardsFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        CreditCardsFilterDto creditCardsFilterDto = (CreditCardsFilterDto) target;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        if (Objects.isNull(creditCardsFilterDto.getValueFromDate()) &&
            Objects.isNull(creditCardsFilterDto.getValueToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
        }
        if (Objects.nonNull(creditCardsFilterDto.getValueFromDate())) {
            if (!creditCardsFilterDto.getValueFromDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_VALUE_FROM_DATE);
            }else {
                YearMonth yearMonth = YearMonth.parse(creditCardsFilterDto.getValueFromDate(), dateTimeFormatter);
                creditCardsFilterDto.setFromDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(creditCardsFilterDto.getValueToDate())) {
            if (!creditCardsFilterDto.getValueToDate().matches(dateRegexp)) {
                errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_VALUE_TO_DATE);
            }else {
                YearMonth yearMonth = YearMonth.parse(creditCardsFilterDto.getValueToDate(), dateTimeFormatter);
                creditCardsFilterDto.setToDate(yearMonth.atEndOfMonth());
            }
        }
        if (Objects.nonNull(creditCardsFilterDto.getFromDate()) &&
            Objects.nonNull(creditCardsFilterDto.getToDate()) &&
            creditCardsFilterDto.getFromDate().equals(creditCardsFilterDto.getToDate())) {
            errors.rejectValue(VALUE_FROM_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_FROM_DATE);
            errors.rejectValue(VALUE_TO_DATE, ERROR_CODE_DIFFERENT_DATES_VALUE_TO_DATE);
        }
    }

}
