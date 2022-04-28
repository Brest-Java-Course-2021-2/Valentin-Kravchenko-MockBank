package com.epam.brest.webapp.validator;

import com.epam.brest.model.CreditCardFilterDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class CreditCardFilterDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardFilterDtoValidator.class);

    @Value("${card.filter.date.regexp}")
    private String dateRegexp;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreditCardFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        CreditCardFilterDto creditCardFilterDto = (CreditCardFilterDto) target;
        if (isNull(creditCardFilterDto.getFromDateValue()) && isNull(creditCardFilterDto.getToDateValue())) {
            errors.rejectValue(FROM_DATE_VALUE, FROM_DATE_VALUE_ERROR_CODE);
            errors.rejectValue(TO_DATE_VALUE, TO_DATE_VALUE_ERROR_CODE);
        }
        if (nonNull(creditCardFilterDto.getFromDateValue()) && nonNull(creditCardFilterDto.getToDateValue()) &&
            creditCardFilterDto.getFromDateValue().equals(creditCardFilterDto.getToDateValue())) {
            errors.rejectValue(FROM_DATE_VALUE, FROM_DATE_ERROR_CODE);
            errors.rejectValue(TO_DATE_VALUE, TO_DATE_ERROR_CODE);
        }
        if (nonNull(creditCardFilterDto.getFromDateValue()) && !creditCardFilterDto.getFromDateValue().matches(dateRegexp)) {
            errors.rejectValue(FROM_DATE_VALUE, FROM_DATE_VALUE_ERROR_CODE);
        }
        if (nonNull(creditCardFilterDto.getToDateValue()) && !creditCardFilterDto.getToDateValue().matches(dateRegexp)) {
            errors.rejectValue(TO_DATE_VALUE, TO_DATE_VALUE_ERROR_CODE);
        }
    }

}
