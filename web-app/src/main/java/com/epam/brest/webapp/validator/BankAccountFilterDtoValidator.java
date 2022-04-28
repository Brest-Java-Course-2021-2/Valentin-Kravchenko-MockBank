package com.epam.brest.webapp.validator;

import com.epam.brest.model.BankAccountFilterDto;
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
public class BankAccountFilterDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountFilterDtoValidator.class);

    @Value("${account.filter.customer.regexp}")
    private String customerRegexp;

    @Value("${account.filter.number.regexp}")
    private String numberRegexp;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccountFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        BankAccountFilterDto bankAccountFilterDto = (BankAccountFilterDto) target;
        if (isNull(bankAccountFilterDto.getNumberPattern()) && isNull(bankAccountFilterDto.getCustomerPattern())) {
            errors.rejectValue(NUMBER_PATTERN, NUMBER_PATTERN_ERROR_CODE);
            errors.rejectValue(CUSTOMER_PATTERN, CUSTOMER_PATTERN_ERROR_CODE);
        }
        if (nonNull(bankAccountFilterDto.getNumberPattern()) && !bankAccountFilterDto.getNumberPattern().matches(numberRegexp)) {
            errors.rejectValue(NUMBER_PATTERN, NUMBER_PATTERN_ERROR_CODE);
        }
        if (nonNull(bankAccountFilterDto.getCustomerPattern()) && !bankAccountFilterDto.getCustomerPattern().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER_PATTERN, CUSTOMER_PATTERN_ERROR_CODE);
        }
    }

}
