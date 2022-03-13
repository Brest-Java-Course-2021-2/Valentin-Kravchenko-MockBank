package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.BankAccountsFilterDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

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
        return BankAccountsFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        BankAccountsFilterDto bankAccountsFilterDto = (BankAccountsFilterDto) target;
        if (Objects.isNull(bankAccountsFilterDto.getNumberPattern()) &&
            Objects.isNull(bankAccountsFilterDto.getCustomerPattern())) {
            errors.rejectValue(NUMBER_PATTERN, ERROR_CODE_ACCOUNT_NUMBER_PATTERN);
            errors.rejectValue(CUSTOMER_PATTERN, ERROR_CODE_CUSTOMER_PATTERN);
        }
        if (Objects.nonNull(bankAccountsFilterDto.getNumberPattern()) &&
            !bankAccountsFilterDto.getNumberPattern().matches(numberRegexp)) {
            errors.rejectValue(NUMBER_PATTERN, ERROR_CODE_ACCOUNT_NUMBER_PATTERN);
        }
        if (Objects.nonNull(bankAccountsFilterDto.getCustomerPattern()) &&
            !bankAccountsFilterDto.getCustomerPattern().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER_PATTERN, ERROR_CODE_CUSTOMER_PATTERN);
        }
    }

}
