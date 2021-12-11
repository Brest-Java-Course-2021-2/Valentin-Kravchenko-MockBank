package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
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
class CreditCardDateRangeDtoValidatorTest {

    private final Validator validator;

    CreditCardDateRangeDtoValidatorTest(@Autowired @Qualifier("creditCardDateRangeDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateDateRangeIsValid(){
        // Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("06/2023");
        creditCardDateRangeDto.setValueToDate("06/2025");
        DataBinder dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 2
        creditCardDateRangeDto.setValueToDate("");
        dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 3
        creditCardDateRangeDto.setValueFromDate("06/2023");
        creditCardDateRangeDto.setValueToDate("");
        dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateDateRangeIsInvalid(){
        // Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("06.2023");
        creditCardDateRangeDto.setValueToDate("06/1025");
        DataBinder dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("06/2023");
        creditCardDateRangeDto.setValueToDate("06/2023");
        dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("23/2023");
        creditCardDateRangeDto.setValueToDate("00/2023");
        dataBinder = new DataBinder(creditCardDateRangeDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
    }

}