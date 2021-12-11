package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.BankAccountFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.CUSTOMER;
import static com.epam.brest.webapp.constant.ControllerConstant.NUMBER;
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
        bankAccountFilterDto.setNumber("BY100TRE");
        bankAccountFilterDto.setCustomer("Petr");
        DataBinder dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 2
        bankAccountFilterDto.setCustomer("");
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        //Case 3
        bankAccountFilterDto.setNumber("");
        bankAccountFilterDto.setCustomer("Petr");
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateIsInvalid() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumber("BY100TRE@");
        bankAccountFilterDto.setCustomer("Petr2");
        DataBinder dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(CUSTOMER));
        //Case 2
        bankAccountFilterDto.setNumber("BY100TRE@");
        bankAccountFilterDto.setCustomer("");
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(NUMBER));
        //Case 3
        bankAccountFilterDto.setNumber("");
        bankAccountFilterDto.setCustomer("Petr2");
        dataBinder = new DataBinder(bankAccountFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(CUSTOMER));
    }

}