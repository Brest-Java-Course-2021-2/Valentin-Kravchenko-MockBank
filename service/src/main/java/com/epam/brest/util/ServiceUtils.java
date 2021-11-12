package com.epam.brest.util;

import java.time.LocalDate;

public final class ServiceUtils {

    private static final int CIRCULATION_IN_YEARS = 3;

    private ServiceUtils() {

    }

    public static LocalDate convertToExpirationDate(LocalDate registrationDate) {
        LocalDate lastDate = registrationDate.withDayOfMonth(registrationDate.lengthOfMonth());
        return lastDate.plusYears(CIRCULATION_IN_YEARS);
    }

}
