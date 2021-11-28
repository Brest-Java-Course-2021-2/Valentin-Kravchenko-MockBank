package com.epam.brest.util;

import com.epam.brest.model.BasicEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

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

    public static <T extends BasicEntity> void copyProperties(T source, T target){
        LOGGER.info("copyProperties(source={}, target={})", source, target);
        Arrays.stream(source.getClass().getDeclaredFields())
              .forEach(field -> {
                  try {
                      field.setAccessible(true);
                      Object value = field.get(source);
                      if (Objects.isNull(value)) {
                          return;
                      }
                      Field targetField = target.getClass().getDeclaredField(field.getName());
                      targetField.setAccessible(true);
                      targetField.set(target, value);
                  } catch (NoSuchFieldException | IllegalAccessException e) {
                      LOGGER.error("copyProperties(error)", e);
                      throw new RuntimeException(e);
                  }
              });
    }

}
