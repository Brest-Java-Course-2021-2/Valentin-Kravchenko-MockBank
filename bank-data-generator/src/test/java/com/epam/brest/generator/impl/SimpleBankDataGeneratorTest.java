package com.epam.brest.generator.impl;

import com.epam.brest.generator.BankDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

import static org.iban4j.CountryCode.BY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SimpleBankDataGenerator.class})
@TestPropertySource(properties = {"spring.output.ansi.enabled=always"})
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

    @Test
    void test() {
        Set<Integer> collection = new HashSet<>();

    }

}