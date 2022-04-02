package com.epam.brest.webapp.validator;

import com.epam.brest.model.CreditCardFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.VALUE_FROM_DATE;
import static com.epam.brest.webapp.constant.ControllerConstant.VALUE_TO_DATE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(locations = {"classpath*:test-validator.xml"})
class CreditCardFilterDtoValidatorTest {

    private final Validator validator;

    CreditCardFilterDtoValidatorTest(@Autowired @Qualifier("creditCardDateRangeDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateDateRangeIsValid(){
        // Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setValueFromDate("06/2023");
        creditCardFilterDto.setValueToDate("06/2025");
        DataBinder dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 2
        creditCardFilterDto.setValueToDate(null);
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 3
        creditCardFilterDto.setValueFromDate("06/2023");
        creditCardFilterDto.setValueToDate(null);
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateDateRangeIsInvalid(){
        // Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setValueFromDate("06.2023");
        creditCardFilterDto.setValueToDate("06/1025");
        DataBinder dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setValueFromDate("06/2023");
        creditCardFilterDto.setValueToDate("06/2023");
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setValueFromDate("23/2023");
        creditCardFilterDto.setValueToDate("00/2023");
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
    }

}