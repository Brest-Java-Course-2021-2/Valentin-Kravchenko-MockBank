package com.epam.brest.dao.util;

import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import static com.epam.brest.dao.constant.ColumnName.ID;
import static com.epam.brest.dao.constant.ColumnName.NUMBER;
import static org.junit.jupiter.api.Assertions.*;

class DaoUtilsTest {

    @Test
    void getSqlParameterSourceWhenIsWrapInPercentsFalse() {
        //Case 1
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setNumber("1");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccount);
        assertEquals(sqlParameterSource.getValue(ID.name()), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER.name()), "1");
        //Case 2
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.getSqlParameterSource(creditCard);
        assertEquals(sqlParameterSource.getValue(ID.name()), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER.name()), "1");
    }

    @Test
    void getSqlParameterSourceWhenIsWrapInPercentsTrue() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumber("number");
        bankAccountFilterDto.setCustomer("customer");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue("NUMBER"), "%number%");
        assertEquals(sqlParameterSource.getValue("CUSTOMER"), "%customer%");
        //Case 2
        bankAccountFilterDto.setCustomer(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        assertEquals(sqlParameterSource.getValue("NUMBER"), "%number%");
    }
                                            
    @Test
    void buildFilterWhereSqlCase() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumber("number");
        bankAccountFilterDto.setCustomer("customer");
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        String template = "BA.$ LIKE :$";
        String filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains("BA.NUMBER LIKE :NUMBER"));
        assertTrue(filterWhereSql.contains("AND"));
        assertTrue(filterWhereSql.contains("BA.CUSTOMER LIKE :CUSTOMER"));
        //Case 2
        bankAccountFilterDto.setCustomer(null);
        sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        filterWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, template);
        assertTrue(filterWhereSql.contains("BA.NUMBER LIKE :NUMBER"));
        assertFalse(filterWhereSql.contains("AND"));
    }

}