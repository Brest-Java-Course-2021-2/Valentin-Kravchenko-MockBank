package com.epam.brest.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUtilsTest {

    @Test
    void convertToExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = ServiceUtils.convertToExpirationDate(currentDate);
        assertEquals(expirationDate.getDayOfMonth(), YearMonth.now().atEndOfMonth().getDayOfMonth());
        assertEquals(currentDate.getMonth(), expirationDate.getMonth());
        int circulationInYears = 3;
        assertEquals(currentDate.getYear(), expirationDate.minusYears(circulationInYears).getYear());
    }

}