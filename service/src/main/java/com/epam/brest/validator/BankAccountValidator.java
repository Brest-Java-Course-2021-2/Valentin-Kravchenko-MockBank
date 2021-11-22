package com.epam.brest.validator;

import com.epam.brest.model.entity.BankAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.epam.brest.constant.ServiceConstant.CUSTOMER;
import static com.epam.brest.constant.ServiceConstant.ERROR_CODE_CUSTOMER;

public class BankAccountValidator implements Validator {


    @Value("${account.customer.regexp}")
    private String customerRegexp;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankAccount bankAccount = (BankAccount) target;
        if (!bankAccount.getCustomer().matches(customerRegexp)) {
            errors.rejectValue(CUSTOMER, ERROR_CODE_CUSTOMER);
        }
    }

}
