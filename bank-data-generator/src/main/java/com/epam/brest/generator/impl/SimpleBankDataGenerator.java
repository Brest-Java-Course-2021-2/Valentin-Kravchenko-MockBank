package com.epam.brest.generator.impl;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.generator.util.GeneratorUtils;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import static org.iban4j.CountryCode.BY;

@Service
public class SimpleBankDataGenerator implements BankDataGenerator {

    @Override
    public String generateIban() {
        return Iban.random(BY).toString();
    }

    @Override
    public String generateCardNumber() {
        return GeneratorUtils.generateCardNumber();
    }

    @Override
    public boolean isCardNumberValid(String cardNumber) {
        return GeneratorUtils.isCardNumberValid(cardNumber);
    }

}
