package com.epam.brest.generator.impl;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.generator.config.BankDataGeneratorConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.iban4j.CountryCode.BY;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BankDataGeneratorConfig.class})
class SimpleBankDataGeneratorTest {

    private final BankDataGenerator bankDataGenerator;

    public SimpleBankDataGeneratorTest(@Autowired BankDataGenerator bankDataGenerator) {
        this.bankDataGenerator = bankDataGenerator;
    }

    @Test
    void generateIban() {
        String iban = bankDataGenerator.generateIban();
        assertTrue(iban.contains(BY.name()));
        int maxIbanLength = 34;
        assertTrue(iban.length() <= maxIbanLength);
    }

    @Test
    void generateCardNumber() {
        String cardNumber1 = bankDataGenerator.generateCardNumber();
        String cardNumber2 = bankDataGenerator.generateCardNumber();
        int cardNumberLength =  16;
        assertEquals(cardNumberLength, cardNumber1.length());
        assertTrue(bankDataGenerator.isCardNumberValid(cardNumber1));
        assertEquals(cardNumberLength, cardNumber2.length());
        assertTrue(bankDataGenerator.isCardNumberValid(cardNumber2));
    }

    @Test
    void cardNumberValidTest() {
        String validCardNumber1 = "4929554996657108";
        String validCardNumber2 = "4024007151271862";
        String validCardNumber3 = "5381714434672717";
        assertTrue(bankDataGenerator.isCardNumberValid(validCardNumber1));
        assertTrue(bankDataGenerator.isCardNumberValid(validCardNumber2));
        assertTrue(bankDataGenerator.isCardNumberValid(validCardNumber3));
    }

    @Test
    void cardNumberInvalidTest() {
        String invalidCardNumber1 = "4929554996657100";
        String invalidCardNumber2 = "4024007151271860";
        String invalidCardNumber3 = "5381714434672710";
        assertFalse(bankDataGenerator.isCardNumberValid(invalidCardNumber1));
        assertFalse(bankDataGenerator.isCardNumberValid(invalidCardNumber2));
        assertFalse(bankDataGenerator.isCardNumberValid(invalidCardNumber3));
    }

}