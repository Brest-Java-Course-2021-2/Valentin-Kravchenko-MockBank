package com.epam.brest.faker.impl;

import com.epam.brest.faker.api.FakerService;
import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.faker.util.FakerServiceUtils;
import com.epam.brest.model.BasicEntity;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.IntFunction;

public abstract class BasicFakerServiceImpl<T extends BasicEntity> implements FakerService<T> {

    private static final Logger LOGGER = LogManager.getLogger(BasicFakerServiceImpl.class);

    public static final String CUSTOMER_TEMPLATE = "%s %s";

    private final FakerSettings fakerSettings;

    public BasicFakerServiceImpl(FakerSettings fakerSettings) {
        this.fakerSettings = fakerSettings;
    }

    @Override
    public List<T> getFakeData(Optional<Integer> amount, Optional<Locale> locale) {
        LOGGER.debug("getFakeData(amount={}, locale={})", amount, locale);
        Faker faker = new Faker(locale.orElse(fakerSettings.getLocale()));
        IntFunction<T> fakerMapper = buildFakerMapper(faker, fakerSettings);
        return FakerServiceUtils.generateFakeData(amount.orElse(fakerSettings.getAmount()), fakerMapper);
    }

    public abstract IntFunction<T> buildFakerMapper(Faker faker, FakerSettings fakerSettings);

}
