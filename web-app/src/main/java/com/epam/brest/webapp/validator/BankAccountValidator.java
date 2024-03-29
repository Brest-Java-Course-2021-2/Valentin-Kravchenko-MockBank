package com.epam.brest.webapp.validator;

import com.epam.brest.model.BankAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.CUSTOMER;
import static com.epam.brest.webapp.constant.ControllerConstant.CUSTOMER_ERROR_CODE;

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
        LOGGER.debug("validate(target={})", target);
        BankAccount bankAccount = (BankAccount) target;
        if (!bankAccount.getCustomer().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER, CUSTOMER_ERROR_CODE);
        }
    }

}
