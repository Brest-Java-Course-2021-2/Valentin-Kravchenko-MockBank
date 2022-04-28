package com.epam.brest.faker.impl;

import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.model.CreditCard;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.IntFunction;

import static com.github.javafaker.CreditCardType.VISA;
import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CreditCardFakerServiceImpl extends BasicFakerServiceImpl<CreditCard> {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardFakerServiceImpl.class);

    public static final String TARGET = "-";

    public static final String REPLACEMENT = "";

    public CreditCardFakerServiceImpl(FakerSettings fakerSettings) {
        super(fakerSettings);
    }

    @Override
    public IntFunction<CreditCard> buildFakerMapper(Faker faker, FakerSettings fakerSettings) {
        LOGGER.trace("buildFakerMapper(faker={}, fakerSettings={})", faker, fakerSettings);
        return i -> {
            CreditCard creditCard = new CreditCard();
            creditCard.setId(i);
            creditCard.setNumber(faker.finance().creditCard(VISA).replace(TARGET, REPLACEMENT));
            double fakerDouble = faker.number().randomDouble(fakerSettings.getBalanceScale(), 0, fakerSettings.getBalanceLimit());
            creditCard.setBalance(new BigDecimal(fakerDouble).setScale(fakerSettings.getBalanceScale(), HALF_UP));
            Date fakerDate = faker.date().between(Date.from(Instant.now()),
                                                  Date.from(Instant.now().plus(fakerSettings.getUnitTimeLimit(), DAYS)));
            LocalDate fakerLocalDate = LocalDate.ofInstant(fakerDate.toInstant(), ZoneId.systemDefault());
            creditCard.setExpirationDate(fakerLocalDate);
            creditCard.setAccountId(i);
            return creditCard;
        };
    }

}
