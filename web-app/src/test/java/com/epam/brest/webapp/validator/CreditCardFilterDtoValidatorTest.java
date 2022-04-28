package com.epam.brest.webapp.validator;

import com.epam.brest.model.CreditCardFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.FROM_DATE_VALUE;
import static com.epam.brest.webapp.constant.ControllerConstant.TO_DATE_VALUE;
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
        creditCardFilterDto.setFromDateValue("06/2023");
        creditCardFilterDto.setToDateValue("06/2025");
        DataBinder dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 2
        creditCardFilterDto.setToDateValue(null);
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 3
        creditCardFilterDto.setFromDateValue("06/2023");
        creditCardFilterDto.setToDateValue(null);
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateDateRangeIsInvalid(){
        // Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDateValue("06.2023");
        creditCardFilterDto.setToDateValue("06/1025");
        DataBinder dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(FROM_DATE_VALUE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(TO_DATE_VALUE));
        // Case 1
        creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDateValue("06/2023");
        creditCardFilterDto.setToDateValue("06/2023");
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(FROM_DATE_VALUE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(TO_DATE_VALUE));
        // Case 1
        creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDateValue("23/2023");
        creditCardFilterDto.setToDateValue("00/2023");
        dataBinder = new DataBinder(creditCardFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(FROM_DATE_VALUE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(TO_DATE_VALUE));
    }

}