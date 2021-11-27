package com.epam.brest.util;

import com.epam.brest.model.BaseEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import static com.epam.brest.constant.ServiceConstant.CIRCULATION_IN_YEARS;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static LocalDate convertToExpirationDate(LocalDate registrationDate) {
        LocalDate lastDate = registrationDate.withDayOfMonth(registrationDate.lengthOfMonth());
        return lastDate.plusYears(CIRCULATION_IN_YEARS);
    }

    public static <T extends BaseEntity> void copyProperties(T source, T target){
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
                      throw new RuntimeException(e);
                  }
              });
    }

}
