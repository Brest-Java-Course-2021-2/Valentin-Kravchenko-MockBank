package com.epam.brest.dao.util;

import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.epam.brest.dao.constant.DaoConstant.AND_DELIMITER;
import static org.junit.jupiter.api.Assertions.*;

class DaoUtilsTest {

    public static final String NUMBER = "number";
    public static final String CUSTOMER = "customer";
    public static final String REGISTRATION_DATE = "registration_date";
    public static final String ID = "id";
    public static final String BALANCE = "balance";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String NUMBER_REGEXP = "(?=.*number).*";
    public static final String CUSTOMER_REGEXP = "(?=.*customer).*";
    public static final String BA_REGEXP = "ba.$ ~ :$";
    public static final String BA_NUMBER_REGEXP_NUMBER = "ba.number ~ :number";
    public static final String BA_CUSTOMER_REGEXP_CUSTOMER = "ba.customer ~ :customer";
    public static final String CC_EXPIRATION_DATE_FROM_DATE = "cc.expiration_date >= :from_date";
    public static final String CC_EXPIRATION_DATE_TO_DATE = "cc.expiration_date <= :to_date";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";

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
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDate(LocalDate.MIN);
        creditCardFilterDto.setToDate(LocalDate.MAX);
        Map<String, String> templateMap = new HashMap<>();
        templateMap.put(FROM_DATE, CC_EXPIRATION_DATE_FROM_DATE);
        templateMap.put(TO_DATE, CC_EXPIRATION_DATE_TO_DATE);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardFilterDto);
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertTrue(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_TO_DATE));
        //Case 2
        creditCardFilterDto.setToDate(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardFilterDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertFalse(filterWhereSql.contains(AND_DELIMITER.trim()));
    }

}