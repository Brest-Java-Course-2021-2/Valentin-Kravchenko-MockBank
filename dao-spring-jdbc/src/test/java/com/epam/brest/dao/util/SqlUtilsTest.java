package com.epam.brest.dao.util;

import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import static com.epam.brest.dao.constant.ColumnName.ACCOUNT_ID;
import static com.epam.brest.dao.constant.ColumnName.ACCOUNT_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlUtilsTest {

    @Test
    void convertToSqlParameterSource() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountId(1);
        bankAccount.setAccountNumber("1");
        SqlParameterSource sqlParameterSource = SqlUtils.extractSqlParameterSource(new BeanPropertySqlParameterSource(bankAccount));
        assertEquals(sqlParameterSource.getValue(ACCOUNT_ID.name()), 1);
        assertEquals(sqlParameterSource.getValue(ACCOUNT_NUMBER.name()), "1");
    }
    
}