package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:test-db.xml", "classpath*:spring-jdbc-dao.xml"})
class SpringJdbcBankAccountDtoDaoTest {

    @Autowired
    private BankAccountDtoDao bankAccountDtoDao;

    @Test
    void getAllContainingTotalCards() {
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllContainingTotalCards();
        assertNotNull(accounts);
        accounts.forEach(account -> assertNotNull(account.getTotalCards()));
    }

}