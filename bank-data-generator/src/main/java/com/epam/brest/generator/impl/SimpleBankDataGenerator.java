package com.epam.brest.generator.impl;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.generator.util.BankDataGeneratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import static org.iban4j.CountryCode.BY;

@Service
public class SimpleBankDataGenerator implements BankDataGenerator {

    private static final Logger LOGGER = LogManager.getLogger(SimpleBankDataGenerator.class);

    @Override
    public String generateIban() {
        LOGGER.debug("generateIban()");
        return Iban.random(BY).toString();
    }

    @Override
    public String generateCardNumber() {
        LOGGER.debug("generateCardNumber()");
        return BankDataGeneratorUtils.generateCardNumber();
    }

    @Override
    public boolean isCardNumberValid(String cardNumber) {
        LOGGER.info("isCardNumberValid(cardNumber={})", cardNumber);
        return BankDataGeneratorUtils.isCardNumberValid(cardNumber);
    }

}
