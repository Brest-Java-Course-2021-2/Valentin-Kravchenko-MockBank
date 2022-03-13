package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankAccountDtoServiceImplIT extends ServiceTestBasic {

    private final BankAccountDtoService bankAccountDtoService;

    BankAccountDtoServiceImplIT(@Autowired BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @Test
    void getAllWithTotalCards() {
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        assertTrue(accounts.size() > 0);
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        //Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String number = "TQ99IK";
        String customer = "Sergeev";
        bankAccountsFilterDto.setNumberPattern(number);
        bankAccountsFilterDto.setCustomerPattern(customer);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(number));
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 2
        bankAccountsFilterDto.setNumberPattern(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 3
        bankAccountsFilterDto.setNumberPattern("BY");
        bankAccountsFilterDto.setCustomerPattern(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 3);
        assertTrue(accounts.stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY")));
    }

}