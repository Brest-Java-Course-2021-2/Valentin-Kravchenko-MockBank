package com.epam.brest.dao.util;

import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.epam.brest.dao.constant.DaoConstant.AND_DELIMITER;
import static org.junit.jupiter.api.Assertions.*;

class DaoUtilsTest {

    public static final String NUMBER = "NUMBER";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String REGISTRATION_DATE = "REGISTRATION_DATE";
    public static final String ID = "ID";
    public static final String BALANCE = "BALANCE";
    public static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    public static final String NUMBER_REGEXP = "(?=.*number).*";
    public static final String CUSTOMER_REGEXP = "(?=.*customer).*";
    public static final String BA_REGEXP = "BA.$ REGEXP :$";
    public static final String BA_NUMBER_REGEXP_NUMBER = "BA.NUMBER REGEXP :NUMBER";
    public static final String BA_CUSTOMER_REGEXP_CUSTOMER = "BA.CUSTOMER REGEXP :CUSTOMER";
    public static final String CC_EXPIRATION_DATE_FROM_DATE = "CC.EXPIRATION_DATE >= :FROM_DATE";
    public static final String CC_EXPIRATION_DATE_TO_DATE = "CC.EXPIRATION_DATE <= :TO_DATE";
    public static final String FROM_DATE = "FROM_DATE";
    public static final String TO_DATE = "TO_DATE";

    @Test
    void getSqlParameterSource() {
        //Case 1
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("1");
        bankAccount.setRegistrationDate(LocalDate.now());
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccount);
        assertEquals(sqlParameterSource.getValue(REGISTRATION_DATE), LocalDate.now());
        assertEquals(sqlParameterSource.getValue(NUMBER), "1");
        assertFalse(sqlParameterSource.hasValue(ID));
        assertFalse(sqlParameterSource.hasValue(CUSTOMER));
        //Case 2
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCard);
        assertEquals(sqlParameterSource.getValue(ID), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER), "1");
        assertFalse(sqlParameterSource.hasValue(EXPIRATION_DATE));
        assertFalse(sqlParameterSource.hasValue(BALANCE));
    }

    @Test
    void getSqlParameterSourceWhenAnnotationSqlRegexpPresents() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER.toLowerCase());
        bankAccountFilterDto.setCustomerPattern(CUSTOMER.toLowerCase());
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER), NUMBER_REGEXP);
        assertEquals(sqlParameterSource.getValue(CUSTOMER), CUSTOMER_REGEXP);
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER), NUMBER_REGEXP);
        assertFalse(sqlParameterSource.hasValue(CUSTOMER));
    }
                                            
    @Test
    void buildFilterWhereSqlInCaseSqlTemplateIsString() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER.toLowerCase());
        bankAccountFilterDto.setCustomerPattern(CUSTOMER.toLowerCase());
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, BA_REGEXP);
        assertTrue(filterWhereSql.contains(BA_NUMBER_REGEXP_NUMBER));
        assertTrue(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertTrue(filterWhereSql.contains(BA_CUSTOMER_REGEXP_CUSTOMER));
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, BA_REGEXP);
        assertTrue(filterWhereSql.contains(BA_NUMBER_REGEXP_NUMBER));
        assertFalse(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertFalse(filterWhereSql.contains(BA_CUSTOMER_REGEXP_CUSTOMER));
    }

    @Test
    void buildFilterWhereSqlInCaseSqlTemplateIsMap() {
        //Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setFromDate(LocalDate.MIN);
        creditCardDateRangeDto.setToDate(LocalDate.MAX);
        Map<String, String> templateMap = new HashMap<>();
        templateMap.put(FROM_DATE, CC_EXPIRATION_DATE_FROM_DATE);
        templateMap.put(TO_DATE, CC_EXPIRATION_DATE_TO_DATE);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardDateRangeDto);
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertTrue(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_TO_DATE));
        //Case 2
        creditCardDateRangeDto.setToDate(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardDateRangeDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertFalse(filterWhereSql.contains(AND_DELIMITER.trim()));
    }

}