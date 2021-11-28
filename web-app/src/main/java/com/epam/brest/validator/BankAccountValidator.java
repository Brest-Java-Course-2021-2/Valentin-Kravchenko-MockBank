package com.epam.brest.validator;

import com.epam.brest.model.entity.BankAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.epam.brest.constant.ControllerConstant.CUSTOMER;
import static com.epam.brest.constant.ControllerConstant.ERROR_CODE_CUSTOMER;

@Component
public class BankAccountValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountValidator.class);

    @Value("${account.customer.regexp}")
    private String customerRegexp;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankAccount bankAccount = (BankAccount) target;
        LOGGER.debug("validate(bankAccount={})", bankAccount);
        if (!bankAccount.getCustomer().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER, ERROR_CODE_CUSTOMER);
        }
    }

}
