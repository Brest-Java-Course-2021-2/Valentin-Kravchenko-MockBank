package com.epam.brest.dao.util;

import com.epam.brest.model.annotation.WrapInPercents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.brest.dao.constant.DaoConstant.*;

public final class DaoUtils {

    private static final Logger LOGGER = LogManager.getLogger(DaoUtils.class);

    private DaoUtils() {
    }

    public static SqlParameterSource getSqlParameterSource(Object entity) {
        LOGGER.info("getSqlParameterSource(entity={})", entity);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Optional.ofNullable(ReflectionUtils.getField(field, entity))
                        .map(value -> wrapInPercents(field, value))
                        .ifPresent(value -> {
                            String param = convertToSnakeCase(field.getName());
                            sqlParameterSource.addValue(param, value);
                        });
        });
        return sqlParameterSource;
    }

    public static String buildDynamicWhereSql(SqlParameterSource sqlParameterSource, String sqlTemplate) {
        LOGGER.info("buildFilterWhereSql(sqlParameterSource={}, sqlTemplate={})", sqlParameterSource, sqlTemplate);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(parameter -> sqlTemplate.replaceAll(SQL_TEMPLATE_REPLACEMENT_REGEX, parameter))
                     .collect(Collectors.joining(AND_DELIMITER));
    }

    private static String convertToSnakeCase(String value) {
         return value.replaceAll(SQL_PARAM_REGEX, SQL_PARAM_REPLACEMENT).toUpperCase();
    }

    private static Object wrapInPercents(Field field, Object value) {
        if (field.isAnnotationPresent(WrapInPercents.class)) {
            String template = field.getAnnotation(WrapInPercents.class).template();
            return String.format(template, value);
        }
        return value;
    }

}
