package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
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
class BankAccountFilterDtoValidatorTest {

    private final Validator validator;

    BankAccountFilterDtoValidatorTest(@Autowired @Qualifier("bankAccountFilterDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateIsValid() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("BY100TRE");
        bankAccountFilterDto.setCustomerPattern("Petr");
        DataBinder dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 3
        bankAccountFilterDto.setNumberPattern(null);
        bankAccountFilterDto.setCustomerPattern("Petr");
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateIsInvalid() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("BY100TRE@");
        bankAccountFilterDto.setCustomerPattern("Petr2");
        DataBinder dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER_PATTERN));
        assertNotNull(dataBinder.getBindingResult().getFieldError(CUSTOMER_PATTERN));
        //Case 2
        bankAccountFilterDto.setNumberPattern("BY100TRE@");
        bankAccountFilterDto.setCustomerPattern(null);
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER_PATTERN));
        //Case 3
        bankAccountFilterDto.setNumberPattern(null);
        bankAccountFilterDto.setCustomerPattern("Petr2");
        dataBinder = new DataBinder(bankAccountFilterDto);
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