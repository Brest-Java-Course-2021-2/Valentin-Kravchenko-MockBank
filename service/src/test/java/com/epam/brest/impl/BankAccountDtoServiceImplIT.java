package com.epam.brest.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.service.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountDtoServiceImplIT extends BasicServiceIT {

    private final BankAccountDtoService bankAccountDtoService;

    BankAccountDtoServiceImplIT(@Autowired BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @Test
    void getAllWithTotalCards() {
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }
    
}