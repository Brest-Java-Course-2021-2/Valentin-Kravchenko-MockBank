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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-validator.xml", "classpath*:test-properties.xml"})
class BankAccountValidatorTest {

    private final Validator validator;

    BankAccountValidatorTest(@Autowired @Qualifier("bankAccountValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void customerIsIncorrectTest() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Sergey Sergeev1");
        DataBinder dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertEquals(dataBinder.getBindingResult().getFieldError().getField(), "customer");
    }

    @Test
    void customerIsCorrectTest() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Sergey Sergeev");
        DataBinder dataBinder = new DataBinder(bankAccount);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

}