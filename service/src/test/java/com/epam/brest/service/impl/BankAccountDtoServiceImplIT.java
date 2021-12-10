package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankAccountDtoServiceImplIT extends ServiceTestConfiguration {

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

    @Test
    void getAllWithTotalCardsByFilter() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String number = "TQ99IK";
        String customer = "Sergeev";
        bankAccountFilterDto.setNumber(number);
        bankAccountFilterDto.setCustomer(customer);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(number));
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 2
        bankAccountFilterDto.setNumber(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 3
        bankAccountFilterDto.setNumber("BY");
        bankAccountFilterDto.setCustomer(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 3);
        assertTrue(accounts.stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY")));
    }

}