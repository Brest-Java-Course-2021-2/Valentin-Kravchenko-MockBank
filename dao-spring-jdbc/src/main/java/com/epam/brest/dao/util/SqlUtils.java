package com.epam.brest.dao.util;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;

public final class SqlUtils {

    private static final String REGEX = "([a-z])([A-Z]+)";
    private static final String REPLACEMENT = "$1_$2";
    private static final String EXCLUDING_PARAM = "class";

    private SqlUtils() {
    }

    public static SqlParameterSource extractSqlParameterSource(BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        Arrays.stream(beanPropertySqlParameterSource.getReadablePropertyNames())
              .filter(param -> !param.equals(EXCLUDING_PARAM))
              .forEach(param -> {
                    Object value = beanPropertySqlParameterSource.getValue(param);
                    String sqlParam = convertToSnakeCase(param).toUpperCase();
                    sqlParameterSource.addValue(sqlParam, value);
                });
        return sqlParameterSource;
    }

    private static String convertToSnakeCase(String param) {
        return param.replaceAll(REGEX, REPLACEMENT);
    }

}
