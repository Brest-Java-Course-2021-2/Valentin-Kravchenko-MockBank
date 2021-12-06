package com.epam.brest.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.epam.brest.constant.ControllerConstant.*;

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
        BankAccountFilterDto bankAccountFilterDto = (BankAccountFilterDto) target;
        if (bankAccountFilterDto.getNumber().isEmpty() &&
            bankAccountFilterDto.getCustomer().isEmpty()) {
            errors.rejectValue(NUMBER, ERROR_CODE_FILTER_NUMBER);
            errors.rejectValue(CUSTOMER, ERROR_CODE_FILTER_CUSTOMER);
        }
        if (!bankAccountFilterDto.getNumber().isEmpty() &&
            !bankAccountFilterDto.getNumber().matches(numberRegexp)) {
            errors.rejectValue(NUMBER, ERROR_CODE_FILTER_NUMBER);
        }
        if (!bankAccountFilterDto.getCustomer().isEmpty() &&
            !bankAccountFilterDto.getCustomer().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER, ERROR_CODE_FILTER_CUSTOMER);
        }
    }

}
