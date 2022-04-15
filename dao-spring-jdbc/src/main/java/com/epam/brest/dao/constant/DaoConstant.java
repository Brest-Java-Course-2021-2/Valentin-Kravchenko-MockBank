package com.epam.brest.dao.constant;

public class DaoConstant {

    public static final String CAMEL_CASE_REGEXP = "([a-z])([A-Z]+)";
    public static final String SPLIT_REGEXP = "\\s+";
    public static final String REPLACEMENT_REGEXP = "\\$";
    public static final String SNAKE_CASE_TEMPLATE = "$1_$2";
    public static final String DOT_CASE_TEMPLATE = "$1.$2";
    public static final String DOT_DELIMITER = ".";
    public static final String AND_DELIMITER = " AND ";
    public static final String REGEXP_SQL_POSTFIX = ".*";
    public static final String POSTFIX_INJECTION_FIELD = "Sql";

}
