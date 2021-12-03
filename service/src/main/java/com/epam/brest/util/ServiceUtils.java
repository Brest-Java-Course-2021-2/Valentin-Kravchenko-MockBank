package com.epam.brest.util;

import com.epam.brest.model.BasicEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static com.epam.brest.constant.ServiceConstant.CIRCULATION_IN_YEARS;

public final class ServiceUtils {

    private static final Logger LOGGER = LogManager.getLogger(ServiceUtils.class);

    private ServiceUtils() {
    }

    public static LocalDate convertToExpirationDate(LocalDate registrationDate) {
        LOGGER.info("convertToExpirationDate(registrationDate={})", registrationDate);
        LocalDate lastDate = registrationDate.withDayOfMonth(registrationDate.lengthOfMonth());
        return lastDate.plusYears(CIRCULATION_IN_YEARS);
    }

    public static <T extends BasicEntity> void copyProperties(T source, T target) {
        LOGGER.info("copyProperties(source={}, target={})", source, target);
        Arrays.stream(source.getClass().getDeclaredFields())
              .forEach(field -> {
                  field.setAccessible(true);
                  Optional.ofNullable(ReflectionUtils.getField(field, source))
                          .ifPresent(value -> {
                              Field targetField = ReflectionUtils.findField(target.getClass(), field.getName());
                              targetField.setAccessible(true);
                              ReflectionUtils.setField(targetField, target, value);
                          });
              });
    }

}
