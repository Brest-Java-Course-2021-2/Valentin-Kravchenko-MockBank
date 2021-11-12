package com.epam.brest.dao.util;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import static com.epam.brest.dao.constant.ColumnName.NUMBER;
import static com.epam.brest.dao.constant.ColumnName.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DaoUtilsTest {

    @Test
    void convertToSqlParameterSource() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setNumber("1");
        SqlParameterSource sqlParameterSource = DaoUtils.extractSqlParameterSource(new BeanPropertySqlParameterSource(bankAccount));
        assertEquals(sqlParameterSource.getValue(ID.name()), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER.name()), "1");
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        creditCard.setNumber("1");
        sqlParameterSource = DaoUtils.extractSqlParameterSource(new BeanPropertySqlParameterSource(creditCard));
        assertEquals(sqlParameterSource.getValue(ID.name()), 1);
        assertEquals(sqlParameterSource.getValue(NUMBER.name()), "1");
    }
    
}