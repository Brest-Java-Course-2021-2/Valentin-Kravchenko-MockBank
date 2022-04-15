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
import static com.epam.brest.dao.constant.DaoConstant.REPLACEMENT_REGEXP;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class DaoUtilsTest {

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String CUSTOMER = "customer";
    public static final String REGISTRATION_DATE = "registration_date";
    public static final String BALANCE = "balance";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String PATTERN = "(?=.*%s).*";
    public static final String BA_WHERE_TEMPLATE = "ba.$ ~ :$";
    public static final String CC_EXPIRATION_DATE_FROM_DATE = "cc.expiration_date >= :from_date";
    public static final String CC_EXPIRATION_DATE_TO_DATE = "cc.expiration_date <= :to_date";

    @Test
    void getSqlParameterSource() {
        //Case 1
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("1");
        bankAccount.setRegistrationDate(LocalDate.now());
        SqlParameterSource sqlParameterSource = DaoUtils.buildSqlParameterSource(bankAccount);
        assertEquals(sqlParameterSource.getValue(NUMBER), bankAccount.getNumber());
        assertEquals(sqlParameterSource.getValue(REGISTRATION_DATE), bankAccount.getRegistrationDate());
        assertFalse(sqlParameterSource.hasValue(ID));
        assertFalse(sqlParameterSource.hasValue(CUSTOMER));
        //Case 2
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.buildSqlParameterSource(creditCard);
        assertEquals(sqlParameterSource.getValue(ID), creditCard.getId());
        assertEquals(sqlParameterSource.getValue(NUMBER), creditCard.getNumber());
        assertFalse(sqlParameterSource.hasValue(EXPIRATION_DATE));
        assertFalse(sqlParameterSource.hasValue(BALANCE));
    }

    @Test
    void getSqlParameterSourceWhenAnnotationConvertToRegexpIsPresent() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER);
        bankAccountFilterDto.setCustomerPattern(CUSTOMER);
        SqlParameterSource sqlParameterSource = DaoUtils.buildSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER), format(PATTERN, NUMBER));
        assertEquals(sqlParameterSource.getValue(CUSTOMER), format(PATTERN, CUSTOMER));
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.buildSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue(NUMBER), format(PATTERN, NUMBER));
        assertFalse(sqlParameterSource.hasValue(CUSTOMER));
    }
                                            
    @Test
    void buildSqlWithDynamicWhereForWhereTemplate() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(NUMBER);
        bankAccountFilterDto.setCustomerPattern(CUSTOMER);
        SqlParameterSource sqlParameterSource = DaoUtils.buildSqlParameterSource(bankAccountFilterDto);
        String filterWhereSql = DaoUtils.buildSqlWithDynamicWhere(sqlParameterSource, BA_WHERE_TEMPLATE);
        assertTrue(filterWhereSql.contains(BA_WHERE_TEMPLATE.replaceAll(REPLACEMENT_REGEXP, NUMBER)));
        assertTrue(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertTrue(filterWhereSql.contains(BA_WHERE_TEMPLATE.replaceAll(REPLACEMENT_REGEXP, CUSTOMER)));
        //Case 2
        bankAccountFilterDto.setCustomerPattern(null);
        sqlParameterSource = DaoUtils.buildSqlParameterSource(bankAccountFilterDto);
        filterWhereSql = DaoUtils.buildSqlWithDynamicWhere(sqlParameterSource, BA_WHERE_TEMPLATE);
        assertTrue(filterWhereSql.contains(BA_WHERE_TEMPLATE.replaceAll(REPLACEMENT_REGEXP, NUMBER)));
        assertFalse(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertFalse(filterWhereSql.contains(BA_WHERE_TEMPLATE.replaceAll(REPLACEMENT_REGEXP, CUSTOMER)));
    }

    @Test
    void buildSqlWithDynamicWhereForWhereTemplates() {
        Map<String, String> whereTemplates = new HashMap<>();
        whereTemplates.put(FROM_DATE, CC_EXPIRATION_DATE_FROM_DATE);
        whereTemplates.put(TO_DATE, CC_EXPIRATION_DATE_TO_DATE);
        //Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDate(LocalDate.MIN);
        creditCardFilterDto.setToDate(LocalDate.MAX);
        SqlParameterSource sqlParameterSource = DaoUtils.buildSqlParameterSource(creditCardFilterDto);
        String filterWhereSql = DaoUtils.buildSqlWithDynamicWhere(sqlParameterSource, whereTemplates);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertTrue(filterWhereSql.contains(AND_DELIMITER.trim()));
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_TO_DATE));
        //Case 2
        creditCardFilterDto.setToDate(null);
        sqlParameterSource = DaoUtils.buildSqlParameterSource(creditCardFilterDto);
        filterWhereSql = DaoUtils.buildSqlWithDynamicWhere(sqlParameterSource, whereTemplates);
        assertTrue(filterWhereSql.contains(CC_EXPIRATION_DATE_FROM_DATE));
        assertFalse(filterWhereSql.contains(AND_DELIMITER.trim()));
    }

}