package com.epam.brest.validator;

import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static com.epam.brest.constant.ControllerConstant.CUSTOMER;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-validator.xml"})
class BankAccountValidatorTest {

    private final Validator validator;

    BankAccountValidatorTest(@Autowired @Qualifier("bankAccountValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateBankAccountIsInvalid() {
        // Case 1
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Sergey Sergeev1");
        DataBinder dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), CUSTOMER);
        // Case 2
        bankAccount.setCustomer("SErgey SergeeV");
        dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), CUSTOMER);
        // Case 3
        bankAccount.setCustomer("S*rgey Sergeev");
        dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), CUSTOMER);
        // Case 4
        bankAccount.setCustomer("Sergey sergeev");
        dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), CUSTOMER);
        // Case 5
        bankAccount.setCustomer("Sergey Sergee" + "v".repeat(60));
        dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), CUSTOMER);
    }

    @Test
    void validateBankAccountIsValid() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Sergey Sergeev");
        DataBinder dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

}