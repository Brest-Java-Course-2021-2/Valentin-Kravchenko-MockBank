package com.epam.brest.service.impl;

import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.BankAccountDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoServiceImplIT {

    private final BankAccountDtoService bankAccountDtoService;

    private List<BankAccountDto> accounts;
    private BankAccountDto firstBankAccount;
    private BankAccountDto lastBankAccount;

    BankAccountDtoServiceImplIT(BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @BeforeEach
    void setup(){
        accounts = bankAccountDtoService.getAllWithTotalCards();
        firstBankAccount = accounts.get(0);
        lastBankAccount = accounts.get(accounts.size() - 1);
    }

    @Test
    void getAllWithTotalCards() {
        assertTrue(accounts.size() > 0);
        assertNotNull(firstBankAccount);
        assertNotNull(lastBankAccount);
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(extractNumberPattern(firstBankAccount.getNumber()));
        bankAccountFilterDto.setCustomerPattern(extractCustomerPattern(firstBankAccount.getCustomer()));
        List<BankAccountDto> accountsByFilter = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accountsByFilter.size(), 1);
        assertEquals(accountsByFilter.get(0), firstBankAccount);
        //case 2
        bankAccountFilterDto.setNumberPattern(null);
        bankAccountFilterDto.setCustomerPattern(extractCustomerPattern(lastBankAccount.getCustomer()));
        accountsByFilter = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accountsByFilter.size(), 1);
        assertEquals(accountsByFilter.get(0), lastBankAccount);
        //case 3
        bankAccountFilterDto.setNumberPattern(extractNumberPattern(firstBankAccount.getNumber()));
        bankAccountFilterDto.setCustomerPattern(null);
        accountsByFilter = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accountsByFilter.size(), 1);
        assertEquals(accountsByFilter.get(0), firstBankAccount);
        //case 4
        bankAccountFilterDto.setNumberPattern("BY");
        bankAccountFilterDto.setCustomerPattern(null);
        accountsByFilter = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accountsByFilter, accounts);
    }

    private String extractCustomerPattern(String customer) {
        return customer.substring(1, customer.length() - 1);
    }

    private String extractNumberPattern(String number) {
        return number.charAt(0) + " " + number.charAt(number.length() - 1);
    }

}