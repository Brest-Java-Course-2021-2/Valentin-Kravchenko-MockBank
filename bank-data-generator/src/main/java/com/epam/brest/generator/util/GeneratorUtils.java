package com.epam.brest.generator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public final class GeneratorUtils {

    private static final Logger LOGGER = LogManager.getLogger(GeneratorUtils.class);

    private static final String ISSUER_IDENTIFICATION_NUMBER = "400000";
    private static final int MAX_RANK = 9;
    private static final Random RANDOM = new Random();

    private GeneratorUtils() {
    }

    public static String generateCardNumber() {
        LOGGER.info("generateCardNumber()");
        String cardSequence = ISSUER_IDENTIFICATION_NUMBER + getSequence();
        int sum = getSum(cardSequence);
        int mod = sum % 10;
        int checkSum = (mod == 0) ? 0 : 10 - mod;
        return cardSequence + checkSum;
    }

    public static boolean isCardNumberValid(String cardNumber) {
        LOGGER.info("isCardNumberValid(cardNumber={})", cardNumber);
        return getSum(cardNumber) % 10 == 0;
    }

    private static String getSequence() {
        LOGGER.trace("getSequence()");
        return RANDOM.ints(MAX_RANK, 0, MAX_RANK + 1)
                     .mapToObj(String::valueOf)
                     .collect(joining());
    }

    private static int getSum(String sequence) {
        LOGGER.trace("getSum(sequence={})", sequence);
        int[] ints = sequence.chars()
                             .map(Character::getNumericValue)
                             .toArray();
        return IntStream.range(0, ints.length)
                        .map(i -> i % 2 == 0 ? 2 * ints[i] : ints[i])
                        .map(i -> i > 9 ? i - 9 : i)
                        .sum();
    }

}
