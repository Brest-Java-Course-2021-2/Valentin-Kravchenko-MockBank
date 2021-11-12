package com.epam.brest.dao.util;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;

public final class DaoUtils {

    private static final String REGEX = "([a-z])([A-Z]+)";
    private static final String REPLACEMENT = "$1_$2";
    private static final String EXCLUDING_PARAM = "class";

    private DaoUtils() {
    }

    public static SqlParameterSource extractSqlParameterSource(BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        Arrays.stream(beanPropertySqlParameterSource.getReadablePropertyNames())
              .filter(param -> !param.equals(EXCLUDING_PARAM))
              .forEach(param -> {
                    Object value = beanPropertySqlParameterSource.getValue(param);
                    String sqlParam = param.replaceAll(REGEX, REPLACEMENT).toUpperCase();
                    sqlParameterSource.addValue(sqlParam, value);
                });
        return sqlParameterSource;
    }

}
