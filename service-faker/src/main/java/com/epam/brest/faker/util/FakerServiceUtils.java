package com.epam.brest.faker.util;

import com.epam.brest.model.BasicEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class FakerServiceUtils {

    private static final Logger LOGGER = LogManager.getLogger(FakerServiceUtils.class);

    private FakerServiceUtils() {
    }

    public static <T extends BasicEntity> List<T> generateFakeData(Integer amount, IntFunction<T> fakerMapper) {
        LOGGER.debug("generateFakeData(amount={}, fakerMapper={})", amount, fakerMapper);
        return IntStream.rangeClosed(1, amount)
                        .mapToObj(fakerMapper)
                        .collect(toList());
    }

}
