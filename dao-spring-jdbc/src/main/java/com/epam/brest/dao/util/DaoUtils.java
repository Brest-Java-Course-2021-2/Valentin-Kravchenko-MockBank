package com.epam.brest.dao.util;

import com.epam.brest.model.annotation.IgnoreAsSqlParameter;
import com.epam.brest.model.annotation.SqlParameter;
import com.epam.brest.model.annotation.ConvertToRegexp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.epam.brest.dao.constant.DaoConstant.*;
import static java.util.stream.Collectors.joining;

public final class DaoUtils {

    private static final Logger LOGGER = LogManager.getLogger(DaoUtils.class);

    private DaoUtils() {
    }

    public static SqlParameterSource buildSqlParameterSource(Object entity) {
        LOGGER.debug("getSqlParameterSource(entity={})", entity);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
                if (field.isAnnotationPresent(IgnoreAsSqlParameter.class)) {
                    return;
                }
                String paramName = getSqlParamName(field);
                ReflectionUtils.makeAccessible(field);
                Optional.ofNullable(ReflectionUtils.getField(field, entity))
                        .ifPresent(value -> {
                            if (field.isAnnotationPresent(ConvertToRegexp.class) && field.getType() == String.class) {
                                String pattern = field.getAnnotation(ConvertToRegexp.class).pattern();
                                value = convertToRegexp((String) value, pattern);
                            }
                            sqlParameterSource.addValue(paramName, value);
                        });
        });
        LOGGER.debug("sqlParameterSource={}", sqlParameterSource);
        return sqlParameterSource;
    }

    public static String buildSqlWithDynamicWhere(SqlParameterSource sqlParameterSource,
                                                  String whereTemplate) {
        LOGGER.debug("buildFilterWhereSql(sqlParameterSource={}, whereTemplate={})",
                     sqlParameterSource, whereTemplate);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(param -> whereTemplate.replaceAll(REPLACEMENT_REGEXP, param))
                     .collect(joining(AND_DELIMITER));
    }

    public static String buildSqlWithDynamicWhere(SqlParameterSource sqlParameterSource,
                                                  Map<String, String> whereTemplates) {
        LOGGER.debug("buildFilterWhereSql(sqlParameterSource={}, whereTemplates={})", sqlParameterSource, whereTemplates);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(whereTemplates::get)
                     .collect(joining(AND_DELIMITER));
    }

    public static String getSqlParamName(Field field) {
        LOGGER.trace("getSqlParamName(field={})", field);
        if (field.isAnnotationPresent(SqlParameter.class)) {
            return field.getAnnotation(SqlParameter.class).value();
        }
        return field.getName();
    }

    public static String convertToSnakeCase(String value) {
        LOGGER.trace("convertToSnakeCase(value={})", value);
        return value.replaceAll(CAMEL_CASE_REGEXP, SNAKE_CASE_TEMPLATE).toLowerCase();
    }

    public static String convertToDotCase(String value) {
        LOGGER.trace("convertToDotCase(value={})", value);
        return value.replaceAll(CAMEL_CASE_REGEXP, DOT_CASE_TEMPLATE).toLowerCase();
    }

    public static Object convertToRegexp(String value, String pattern) {
        LOGGER.trace("convertToRegexp(value={}, pattern={})", value, value);
        String regexp = Arrays.stream(value.split(SPLIT_REGEXP))
                              .map(exp -> String.format(pattern, exp))
                              .collect(joining());
        return regexp + REGEXP_SQL_POSTFIX;
    }

}
