package com.epam.brest.service.impl;

import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoServiceImplIT {

    private final BankAccountDtoService bankAccountDtoService;

    BankAccountDtoServiceImplIT(BankAccountDtoService bankAccountDtoService) {
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