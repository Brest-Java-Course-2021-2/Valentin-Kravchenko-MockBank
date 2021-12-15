package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

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
        if (bankAccountFilterDto.getNumberPattern().isEmpty() &&
            bankAccountFilterDto.getCustomerPattern().isEmpty()) {
            errors.rejectValue(NUMBER_PATTERN, ERROR_CODE_ACCOUNT_NUMBER_PATTERN);
            errors.rejectValue(CUSTOMER_PATTERN, ERROR_CODE_CUSTOMER_PATTERN);
        }
        if (!bankAccountFilterDto.getNumberPattern().isEmpty() &&
            !bankAccountFilterDto.getNumberPattern().matches(numberRegexp)) {
            errors.rejectValue(NUMBER_PATTERN, ERROR_CODE_ACCOUNT_NUMBER_PATTERN);
        }
        if (!bankAccountFilterDto.getCustomerPattern().isEmpty() &&
            !bankAccountFilterDto.getCustomerPattern().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER_PATTERN, ERROR_CODE_CUSTOMER_PATTERN);
        }
    }

}
