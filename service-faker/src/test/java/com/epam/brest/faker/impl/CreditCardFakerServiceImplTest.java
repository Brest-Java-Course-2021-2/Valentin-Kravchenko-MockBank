package com.epam.brest.faker.impl;

import com.epam.brest.faker.api.FakerService;
import com.epam.brest.faker.config.FakerTestConfig;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {FakerTestConfig.class},
                properties = {"spring.output.ansi.enabled=always"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardFakerServiceImplTest {

    public static final int TEST_DATA_VOLUME = 10;

    private final FakerService<CreditCard> fakerService;

    public CreditCardFakerServiceImplTest(FakerService<CreditCard> fakerService) {
        this.fakerService = fakerService;
    }

    @Test
    void getFakeData() {
        List<CreditCard> fakeData = fakerService.getFakeData(Optional.of(TEST_DATA_VOLUME));
        assertEquals(TEST_DATA_VOLUME, fakeData.size());
        assertTrue(fakeData.stream().allMatch(Objects::nonNull));
    }

}