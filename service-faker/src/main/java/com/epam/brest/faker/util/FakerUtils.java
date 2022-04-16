package com.epam.brest.faker.util;

import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.model.BankAccountDto;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class FakerUtils {

    private static final Logger LOGGER = LogManager.getLogger(FakerUtils.class);

    public static final String CUSTOMER_TEMPLATE = "%s %s";

    private FakerUtils() {
    }

    public static List<BankAccountDto> generateFakeBankAccountDtoData(FakerSettings fakerSettings) {
        LOGGER.debug("generateFakeBankAccountDtoData(fakerSettings={})", fakerSettings);
        Faker faker = new Faker(fakerSettings.getLocale());
        return IntStream.rangeClosed(1, fakerSettings.getDataVolume())
                        .mapToObj(i -> {
                            BankAccountDto bankAccountDto = new BankAccountDto();
                            bankAccountDto.setId(i);
                            bankAccountDto.setCustomer(format(CUSTOMER_TEMPLATE, faker.name().firstName(), faker.name().lastName()));
                            bankAccountDto.setNumber(faker.finance().iban());
                            Date fakerDate = faker.date().between(Date.from(Instant.now().minus(fakerSettings.getAmountUnitTimeToSubtract(), ChronoUnit.DAYS)),
                                                                  Date.from(Instant.now()));
                            LocalDate fakerLocalDate = LocalDate.ofInstant(fakerDate.toInstant(), ZoneId.systemDefault());
                            bankAccountDto.setRegistrationDate(fakerLocalDate);
                            bankAccountDto.setTotalCards(faker.number().numberBetween(fakerSettings.getAccountMinTotalCards(),
                                                                                      fakerSettings.getAccountMaxTotalCards()));
                            return bankAccountDto;
                        })
                        .collect(toList());
    }

}
