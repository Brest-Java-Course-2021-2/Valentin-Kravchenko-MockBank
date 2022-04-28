package com.epam.brest.webapp.validator;

import com.epam.brest.model.BankAccountFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static com.epam.brest.webapp.constant.ControllerConstant.CUSTOMER_PATTERN;
import static com.epam.brest.webapp.constant.ControllerConstant.NUMBER_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(locations = {"classpath*:test-validator.xml"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountFilterDtoValidatorTest {

    public static final String TEST_NUMBER_PATTERN = "BY100TRE";
    public static final String TEST_CUSTOMER_PATTERN = "Petr";

    private final Validator validator;

    BankAccountFilterDtoValidatorTest(Validator bankAccountFilterDtoValidator) {
        this.validator = bankAccountFilterDtoValidator;
    }

    @Test
    void validateSuccessful() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(TEST_NUMBER_PATTERN);
        bankAccountFilterDto.setCustomerPattern(TEST_CUSTOMER_PATTERN);
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
    void validateFail() {
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

}