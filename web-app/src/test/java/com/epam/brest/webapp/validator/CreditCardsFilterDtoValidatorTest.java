package com.epam.brest.webapp.validator;

import com.epam.brest.model.dto.CreditCardsFilterDto;
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
class CreditCardsFilterDtoValidatorTest {

    private final Validator validator;

    CreditCardsFilterDtoValidatorTest(@Autowired @Qualifier("creditCardDateRangeDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateDateRangeIsValid(){
        // Case 1
        CreditCardsFilterDto creditCardsFilterDto = new CreditCardsFilterDto();
        creditCardsFilterDto.setValueFromDate("06/2023");
        creditCardsFilterDto.setValueToDate("06/2025");
        DataBinder dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 2
        creditCardsFilterDto.setValueToDate(null);
        dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 3
        creditCardsFilterDto.setValueFromDate("06/2023");
        creditCardsFilterDto.setValueToDate(null);
        dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void validateDateRangeIsInvalid(){
        // Case 1
        CreditCardsFilterDto creditCardsFilterDto = new CreditCardsFilterDto();
        creditCardsFilterDto.setValueFromDate("06.2023");
        creditCardsFilterDto.setValueToDate("06/1025");
        DataBinder dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardsFilterDto = new CreditCardsFilterDto();
        creditCardsFilterDto.setValueFromDate("06/2023");
        creditCardsFilterDto.setValueToDate("06/2023");
        dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
        // Case 1
        creditCardsFilterDto = new CreditCardsFilterDto();
        creditCardsFilterDto.setValueFromDate("23/2023");
        creditCardsFilterDto.setValueToDate("00/2023");
        dataBinder = new DataBinder(creditCardsFilterDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_FROM_DATE));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_TO_DATE));
    }

}