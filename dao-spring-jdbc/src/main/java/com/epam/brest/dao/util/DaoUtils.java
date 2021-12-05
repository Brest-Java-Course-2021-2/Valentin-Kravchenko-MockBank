package com.epam.brest.dao.util;

import com.epam.brest.model.annotation.ExcludeFromSql;
import com.epam.brest.model.annotation.WrapInPercentSigns;
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
                field.setAccessible(true);
                Optional.ofNullable(ReflectionUtils.getField(field, entity))
                        .map(value -> wrapInPercentSigns(field, value))
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
                     .map(param -> sqlTemplate.replaceAll(SQL_TEMPLATE_REPLACEMENT_REGEX, param))
                     .collect(Collectors.joining(AND_DELIMITER));
    }

    public static String buildDynamicWhereSql(SqlParameterSource sqlParameterSource, Map<String, String> sqlTemplateMap) {
        LOGGER.info("buildFilterWhereSql(sqlParameterSource={}, sqlTemplateMap={})", sqlParameterSource, sqlTemplateMap);
        Objects.requireNonNull(sqlParameterSource.getParameterNames());
        return Arrays.stream(sqlParameterSource.getParameterNames())
                     .map(sqlTemplateMap::get)
                     .collect(Collectors.joining(AND_DELIMITER));
    }

    private static String convertToSnakeCase(String value) {
         return value.replaceAll(SQL_PARAM_REGEX, SQL_PARAM_REPLACEMENT).toUpperCase();
    }

    private static Object wrapInPercentSigns(Field field, Object value) {
        if (field.isAnnotationPresent(WrapInPercentSigns.class)) {
            String template = field.getAnnotation(WrapInPercentSigns.class).template();
            return String.format(template, value);
        }
        return value;
    }

}
