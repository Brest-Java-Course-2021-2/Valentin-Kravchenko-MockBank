package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.BankAccountsFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(locations = {"classpath*:test-validator.xml"})
class BankAccountsFilterDtoValidatorTest {

    private final Validator validator;

    BankAccountsFilterDtoValidatorTest(@Autowired @Qualifier("bankAccountFilterDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateIsValid() {
        //Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        bankAccountsFilterDto.setNumberPattern("BY100TRE");
        bankAccountsFilterDto.setCustomerPattern("Petr");
        DataBinder dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 2
        bankAccountsFilterDto.setCustomerPattern(null);
        dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 3
        bankAccountsFilterDto.setNumberPattern(null);
        bankAccountsFilterDto.setCustomerPattern("Petr");
        dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateIsInvalid() {
        //Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        bankAccountsFilterDto.setNumberPattern("BY100TRE@");
        bankAccountsFilterDto.setCustomerPattern("Petr2");
        DataBinder dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER_PATTERN));
        assertNotNull(dataBinder.getBindingResult().getFieldError(CUSTOMER_PATTERN));
        //Case 2
        bankAccountsFilterDto.setNumberPattern("BY100TRE@");
        bankAccountsFilterDto.setCustomerPattern(null);
        dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER_PATTERN));
        //Case 3
        bankAccountsFilterDto.setNumberPattern(null);
        bankAccountsFilterDto.setCustomerPattern("Petr2");
        dataBinder = new DataBinder(bankAccountsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(CUSTOMER_PATTERN));
    }

    @Test
    void temp() {
       BigDecimal a = new BigDecimal("0.00");
        int i = a.compareTo(BigDecimal.valueOf(0));
        System.out.println(i);


    }

}