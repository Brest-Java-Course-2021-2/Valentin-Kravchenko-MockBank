package com.epam.brest.webapp.validator;

import com.epam.brest.model.CreditCardTransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.Locale;

import static com.epam.brest.webapp.constant.ControllerConstant.TARGET_CARD_NUMBER;
import static com.epam.brest.webapp.constant.ControllerConstant.VALUE_SUM_OF_MONEY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(locations = {"classpath*:test-validator.xml"})
class CreditCardTransactionDtoValidatorTest {

    public static final String RU = "ru";

    private final Validator validator;

    CreditCardTransactionDtoValidatorTest(@Autowired @Qualifier("creditCardTransactionDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void validateCreditCardIsInvalid() {
        // Case 1
        String invalidCardNumber = "4929554996657100";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(invalidCardNumber);
        creditCardTransactionDto.setValueSumOfMoney("100,223");
        creditCardTransactionDto.setLocale(new Locale(RU));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_SUM_OF_MONEY));
        // Case 2
        creditCardTransactionDto.setTargetCardNumber("492955499665710w");
        creditCardTransactionDto.setValueSumOfMoney("1000333444,2");
        creditCardTransactionDto.setLocale(new Locale(RU));
        dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_SUM_OF_MONEY));
        // Case 3
        String validCardNumber = "4024007151271862";
        creditCardTransactionDto.setSourceCardNumber(validCardNumber);
        creditCardTransactionDto.setTargetCardNumber(validCardNumber);
        creditCardTransactionDto.setValueSumOfMoney("-1000");
        creditCardTransactionDto.setLocale(new Locale(RU));
        dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_SUM_OF_MONEY));
        // Case 4
        creditCardTransactionDto.setTargetCardNumber("4929554$96657100");
        creditCardTransactionDto.setValueSumOfMoney("1000.55");
        creditCardTransactionDto.setLocale(new Locale(RU));
        dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(VALUE_SUM_OF_MONEY));
    }

    @Test
    void validateCreditCardIsValid() {
        // Case 1
        String validCardNumber = "4929554996657108";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(validCardNumber);
        creditCardTransactionDto.setValueSumOfMoney("2005,55");
        creditCardTransactionDto.setLocale(new Locale(RU));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
        // Case 2
        String validCardNumber1 = "4929554996657108";
        String validCardNumber2 = "4024007151271862";
        creditCardTransactionDto.setSourceCardNumber(validCardNumber1);
        creditCardTransactionDto.setTargetCardNumber(validCardNumber2);
        creditCardTransactionDto.setValueSumOfMoney("50005");
        creditCardTransactionDto.setLocale(new Locale(RU));
        dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

}