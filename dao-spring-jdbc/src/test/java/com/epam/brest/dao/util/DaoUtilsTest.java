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

    @Test
    void getSqlParameterSource() {
        //Case 1
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("1");
        bankAccount.setRegistrationDate(LocalDate.now());
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccount);
        assertEquals(sqlParameterSource.getValue("REGISTRATION_DATE"), LocalDate.now());
        assertEquals(sqlParameterSource.getValue("NUMBER"), "1");
        assertFalse(sqlParameterSource.hasValue("ID"));
        assertFalse(sqlParameterSource.hasValue("CUSTOMER"));
        //Case 2
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCard);
        assertEquals(sqlParameterSource.getValue("ID"), 1);
        assertEquals(sqlParameterSource.getValue("NUMBER"), "1");
        assertFalse(sqlParameterSource.hasValue("EXPIRATION_DATE"));
        assertFalse(sqlParameterSource.hasValue("BALANCE"));
    }

    @Test
    void getSqlParameterSourceWhenAnnotationIsWrapInPercentSignsPresents() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("number");
        bankAccountFilterDto.setCustomerPattern("customer");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue("NUMBER"), "%number%");
        assertEquals(sqlParameterSource.getValue("CUSTOMER"), "%customer%");
        //Case 2
        bankAccountFilterDto.setCustomerPattern("");
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue("NUMBER"), "%number%");
        assertFalse(sqlParameterSource.hasValue("CUSTOMER"));
    }
                                            
    @Test
    void buildFilterWhereSqlForCaseSqlTemplateIsString() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("number");
        bankAccountFilterDto.setCustomerPattern("customer");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        String template = "BA.$ LIKE :$";
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains("BA.NUMBER LIKE :NUMBER"));
        assertTrue(filterWhereSql.contains("AND"));
        assertTrue(filterWhereSql.contains("BA.CUSTOMER LIKE :CUSTOMER"));
        //Case 2
        bankAccountFilterDto.setCustomerPattern("");
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains("BA.NUMBER LIKE :NUMBER"));
        assertFalse(filterWhereSql.contains("AND"));
    }

    @Test
    void buildFilterWhereSqlForCaseForSqlTemplateIsMap() {
        //Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setFromDate(LocalDate.MIN);
        creditCardDateRangeDto.setToDate(LocalDate.MAX);
        Map<String, String> templateMap = new HashMap<>();
        templateMap.put("FROM_DATE", "CC.EXPIRATION_DATE >= :FROM_DATE");
        templateMap.put("TO_DATE", "CC.EXPIRATION_DATE <= :TO_DATE");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardDateRangeDto);
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains("CC.EXPIRATION_DATE >= :FROM_DATE"));
        assertTrue(filterWhereSql.contains("AND"));
        assertTrue(filterWhereSql.contains("CC.EXPIRATION_DATE <= :TO_DATE"));
        //Case 2
        creditCardDateRangeDto.setToDate(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardDateRangeDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, templateMap);
        assertTrue(filterWhereSql.contains("CC.EXPIRATION_DATE >= :FROM_DATE"));
        assertFalse(filterWhereSql.contains("AND"));
    }

}