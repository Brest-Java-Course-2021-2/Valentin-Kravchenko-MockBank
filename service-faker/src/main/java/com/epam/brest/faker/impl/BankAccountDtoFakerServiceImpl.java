package com.epam.brest.faker.impl;

import com.epam.brest.faker.api.FakerService;
import com.epam.brest.faker.config.FakerSettings;
import com.epam.brest.faker.util.FakerUtils;
import com.epam.brest.model.BankAccountDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BankAccountDtoFakerServiceImpl implements FakerService<BankAccountDto> {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoFakerServiceImpl.class);

    private final FakerSettings fakerSettings;

    public BankAccountDtoFakerServiceImpl(FakerSettings fakerSettings) {
        this.fakerSettings = fakerSettings;
    }

    @Override
    public List<BankAccountDto> getFakeData(Optional<Integer> dataVolume, Optional<Locale> locale) {
        LOGGER.debug("getFakeData(dataVolume={}, locale={})", dataVolume, locale);
        dataVolume.ifPresent(fakerSettings::setDataVolume);
        locale.ifPresent(fakerSettings::setLocale);
        return FakerUtils.generateFakeBankAccountDtoData(fakerSettings);
    }

}
