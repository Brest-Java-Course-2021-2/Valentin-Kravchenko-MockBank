package com.epam.brest.faker.impl;

import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.model.BankAccount;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.IntFunction;

import static java.lang.String.format;

@Service
public class BankAccountFakerServiceImpl extends BasicFakerServiceImpl<BankAccount> {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountFakerServiceImpl.class);

    public BankAccountFakerServiceImpl(FakerSettings fakerSettings) {
        super(fakerSettings);
    }

    @Override
    public IntFunction<BankAccount> buildFakerMapper(Faker faker, FakerSettings fakerSettings) {
        LOGGER.trace("buildFakerMapper(faker={}, fakerSettings={})", faker, fakerSettings);
        return i -> {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId(i);
            bankAccount.setCustomer(format(CUSTOMER_TEMPLATE, faker.name().firstName(), faker.name().lastName()));
            bankAccount.setNumber(faker.finance().iban().toUpperCase());
            Date fakerDate = faker.date().between(Date.from(Instant.now().minus(fakerSettings.getUnitTimeLimit(), ChronoUnit.DAYS)),
                                                  Date.from(Instant.now()));
            LocalDate fakerLocalDate = LocalDate.ofInstant(fakerDate.toInstant(), ZoneId.systemDefault());
            bankAccount.setRegistrationDate(fakerLocalDate);
            return bankAccount;
        };
    }

}
