package com.epam.brest.faker.util;

import com.epam.brest.model.BasicEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class FakerUtils {

    private static final Logger LOGGER = LogManager.getLogger(FakerUtils.class);

    private FakerUtils() {
    }

    public static <T extends BasicEntity> List<T> generateFakeData(Integer dataVolume, IntFunction<T> fakerMapper) {
        LOGGER.debug("generateFakeData(dataVolume={}, fakerMapper={})", dataVolume, fakerMapper);
        return IntStream.rangeClosed(1, dataVolume)
                        .mapToObj(fakerMapper)
                        .collect(toList());
    }

}
