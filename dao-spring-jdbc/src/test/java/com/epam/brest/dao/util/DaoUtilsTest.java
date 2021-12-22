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

import static org.junit.jupiter.api.Assertions.*;

class DaoUtilsTest {

    public static final String NUMBER = "number";
    public static final String CUSTOMER = "customer";
    public static final String NUMBER_UPPER_CASE = "NUMBER";
    public static final String CUSTOMER_UPPER_CASE = "CUSTOMER";
    public static final String REGISTRATION_DATE_UPPER_CASE = "REGISTRATION_DATE";
    public static final String ID_UPPER_CASE = "ID";
    public static final String AND = "AND";
    public static final String BA_NUMBER_LIKE_NUMBER = "BA.NUMBER LIKE :NUMBER";
    public static final String BA_CUSTOMER_LIKE_CUSTOMER = "BA.CUSTOMER LIKE :CUSTOMER";
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
        assertEquals(sqlParameterSource.getValue(REGISTRATION_DATE_UPPER_CASE), LocalDate.now());
        assertEquals(sqlParameterSource.getValue(NUMBER_UPPER_CASE), "1");
        assertFalse(sqlParameterSource.hasValue(ID_UPPER_CASE));
        assertFalse(sqlParameterSource.hasValue(CUSTOMER_UPPER_CASE));
        //Case 2
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCard);
        assertEquals(sqlParameterSource.getValue("ID"), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER_UPPER_CASE), "1");
        assertFalse(sqlParameterSource.hasValue("EXPIRATION_DATE"));
        assertFalse(sqlParameterSource.hasValue("BALANCE"));
    }

    @Test
    void getSqlParameterSourceWhenAnnotationIsWrapInPercentSignsPresents() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER);
        bankAccountFilterDto.setCustomerPattern(CUSTOMER);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER_UPPER_CASE), "%number%");
        assertEquals(sqlParameterSource.getValue(CUSTOMER_UPPER_CASE), "%customer%");
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER_UPPER_CASE), "%number%");
        assertFalse(sqlParameterSource.hasValue(CUSTOMER_UPPER_CASE));
    }
                                            
    @Test
    void buildFilterWhereSqlForCaseSqlTemplateIsString() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER);
        bankAccountFilterDto.setCustomerPattern(CUSTOMER);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        String template = "BA.$ LIKE :$";
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains(BA_NUMBER_LIKE_NUMBER));
        assertTrue(filterWhereSql.contains(AND));
        assertTrue(filterWhereSql.contains(BA_CUSTOMER_LIKE_CUSTOMER));
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains(BA_NUMBER_LIKE_NUMBER));
        assertFalse(filterWhereSql.contains(AND));
    }

    @Test
    void buildFilterWhereSqlForCaseForSqlTemplateIsMap() {
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
        assertTrue(filterWhereSql.contains(AND));
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_TO_DATE));
        //Case 2
        creditCardDateRangeDto.setToDate(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardDateRangeDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertFalse(filterWhereSql.contains(AND));
    }

}