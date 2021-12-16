package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
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
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String number = "TQ99IK";
        String customer = "Sergeev";
        bankAccountFilterDto.setNumberPattern(number);
        bankAccountFilterDto.setCustomerPattern(customer);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(number));
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 2
        bankAccountFilterDto.setNumberPattern(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 3
        bankAccountFilterDto.setNumberPattern("BY");
        bankAccountFilterDto.setCustomerPattern(null);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 3);
        assertTrue(accounts.stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY")));
    }

}