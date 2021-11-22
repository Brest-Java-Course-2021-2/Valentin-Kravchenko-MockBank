package com.epam.brest.validator;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

import static com.epam.brest.constant.ServiceConstant.SUM_OF_MONEY;
import static com.epam.brest.constant.ServiceConstant.TARGET_CARD_NUMBER;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-validator.xml", "classpath*:test-properties.xml"})
class CreditCardTransactionDtoValidatorTest {

    private final Validator validator;

    CreditCardTransactionDtoValidatorTest(@Autowired @Qualifier("creditCardTransactionDtoValidator") Validator validator) {
        this.validator = validator;
    }

    @Test
    void creditCardIsInvalidCase1() {
        String invalidCardNumber = "4929554996657100";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(invalidCardNumber);
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("100.223"));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(SUM_OF_MONEY));
    }

    @Test
    void creditCardIsInvalidCase2() {
        String invalidCardNumber = "492955499665710w";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(invalidCardNumber);
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("1000333444.2"));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
        assertNotNull(dataBinder.getBindingResult().getFieldError(SUM_OF_MONEY));
    }

    @Test
    void creditCardIsInvalidCase3() {
        String validCardNumber = "4024007151271862";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(validCardNumber);
        creditCardTransactionDto.setTargetCardNumber(validCardNumber);
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("5005"));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertTrue(dataBinder.getBindingResult().hasErrors());
        assertNotNull(dataBinder.getBindingResult().getFieldError(TARGET_CARD_NUMBER));
    }

    @Test
    void creditCardIsValidCase1() {
        String validCardNumber = "4929554996657108";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(validCardNumber);
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("2005.55"));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

    @Test
    void creditCardIsValidCase2() {
        String validCardNumber1 = "4929554996657108";
        String validCardNumber2 = "4024007151271862";
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(validCardNumber1);
        creditCardTransactionDto.setTargetCardNumber(validCardNumber2);
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("5005"));
        DataBinder dataBinder = new DataBinder(creditCardTransactionDto);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        assertFalse(dataBinder.getBindingResult().hasErrors());
    }

}