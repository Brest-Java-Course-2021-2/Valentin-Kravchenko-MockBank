package com.epam.brest.dao.util;

import com.epam.brest.model.annotation.ExcludeFromSql;
import com.epam.brest.model.annotation.SqlColumn;
import com.epam.brest.model.annotation.SqlRegexp;
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
                if (field.isAnnotationPresent(ExcludeFromSql.class)) {
                    return;
                }
                String param = getParamName(field);
                field.setAccessible(true);
                Optional.ofNullable(ReflectionUtils.getField(field, entity))
                        .map(value -> convertToSqlRegexp(field, value))
                        .ifPresent(value -> sqlParameterSource.addValue(param, value));
        });
        return sqlParameterSource;
    }

    public static String buildDynamicWhereSql(SqlParameterSource sqlParameterSource, String sqlTemplate) {
        LOGGER.info("buildFilterWhereSql(sqlParameterSource={}, sqlTemplate={})", sqlParameterSource, sqlTemplate);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(param -> sqlTemplate.replaceAll(REPLACEMENT_REGEXP, param))
                     .collect(Collectors.joining(AND_DELIMITER));
    }

    public static String buildDynamicWhereSql(SqlParameterSource sqlParameterSource, Map<String, String> sqlTemplateMap) {
        LOGGER.info("buildFilterWhereSql(sqlParameterSource={}, sqlTemplateMap={})", sqlParameterSource, sqlTemplateMap);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(sqlTemplateMap::get)
                     .collect(Collectors.joining(AND_DELIMITER));
    }

    private static String getParamName(Field field) {
        if (field.isAnnotationPresent(SqlColumn.class)) {
            String name = field.getAnnotation(SqlColumn.class).value();
            return name.toUpperCase();
        }
        return convertToSnakeCase(field.getName());
    }

    private static String convertToSnakeCase(String value) {
         return value.replaceAll(CAMEL_CASE_REGEXP, SNAKE_CASE_TEMPLATE).toUpperCase();
    }

    private static Object convertToSqlRegexp(Field field, Object value) {
        if (field.isAnnotationPresent(SqlRegexp.class)) {
            if (!(value instanceof String)) {
                return value;
            }
            String strValue = (String) value;
            String pattern = field.getAnnotation(SqlRegexp.class).pattern();
            String regexp = Arrays.stream(strValue.split(SPLIT_REGEXP))
                                  .map(exp -> String.format(pattern, exp))
                                  .collect(Collectors.joining());
            return regexp + SQL_REGEXP_POSTFIX;
        }
        return value;
    }

}
