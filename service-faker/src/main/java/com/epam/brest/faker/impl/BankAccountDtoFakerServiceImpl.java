package com.epam.brest.faker.impl;

import com.epam.brest.faker.api.FakerService;
import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.faker.util.FakerServiceUtils;
import com.epam.brest.model.BankAccountDto;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.IntFunction;

import static java.lang.String.format;

@Service
public class BankAccountDtoFakerServiceImpl implements FakerService<BankAccountDto> {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoFakerServiceImpl.class);

    public static final String CUSTOMER_TEMPLATE = "%s %s";

    private final FakerSettings fakerSettings;

    public BankAccountDtoFakerServiceImpl(FakerSettings fakerSettings) {
        this.fakerSettings = fakerSettings;
    }

    @Override
    public List<BankAccountDto> getFakeData(Optional<Integer> amount, Optional<Locale> locale) {
        LOGGER.debug("getFakeData(amount={}, locale={})", amount, locale);
        Faker faker = new Faker(locale.orElse(fakerSettings.getLocale()));
        IntFunction<BankAccountDto> fakerMapper = buildFakerMapper(faker, fakerSettings);
        return FakerServiceUtils.generateFakeData(amount.orElse(fakerSettings.getAmount()), fakerMapper);
    }

    private IntFunction<BankAccountDto> buildFakerMapper(Faker faker, FakerSettings fakerSettings) {
        return i -> {
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
        };
    }

}
