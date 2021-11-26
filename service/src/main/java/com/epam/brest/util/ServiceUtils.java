package com.epam.brest.util;

import java.time.LocalDate;

import static com.epam.brest.constant.ServiceConstant.*;

public final class ServiceUtils {

    private ServiceUtils() {

    }

    public static LocalDate convertToExpirationDate(LocalDate registrationDate) {
        LocalDate lastDate = registrationDate.withDayOfMonth(registrationDate.lengthOfMonth());
        return lastDate.plusYears(CIRCULATION_IN_YEARS);
    }

}
