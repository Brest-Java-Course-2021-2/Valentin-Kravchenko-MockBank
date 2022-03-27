package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoRestControllerIT extends RestControllerTestBasic {

    public static final String CUSTOMER_SEARCH_PATTERN_IS_INCORRECT = "Customer search pattern is incorrect!";
    public static final String ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT = "Account number search pattern is incorrect!";
    public static final String ACCOUNTS_ENDPOINT = "/accounts";

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoRestControllerIT(ObjectMapper objectMapper,
                                          MockMvc mockMvc,
                                          BankAccountDtoService bankAccountDtoServiceImpl) {
        super(mockMvc, objectMapper);
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
    }

    @Test
    void accountsGET() throws Exception {
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        int lastIdx = accounts.size() - 1;
        performGetAndExpectStatusOk("/accounts")
               .andExpect(jsonPath("$.size()", is(accounts.size())))
               .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())))
               .andExpect(jsonPath("$[" + lastIdx + "].id", is(accounts.get(lastIdx).getId())));
    }

    @Test
    void accountsPOST() throws Exception {
        // Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        bankAccountsFilterDto.setNumberPattern("BY");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        int lastIdx = accounts.size() - 1;
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountsFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())))
                    .andExpect(jsonPath("$[" + lastIdx + "].id", is(accounts.get(lastIdx).getId())));
        // Case 2
        bankAccountsFilterDto.setNumberPattern("TQ99IK");
        bankAccountsFilterDto.setCustomerPattern("Sergeev");
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountsFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())));
    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String numberPattern = "EEEEE";
        String customerPattern = "Sergeenko";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        bankAccountsFilterDto.setCustomerPattern(customerPattern);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountsFilterDto)
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void accountsPOSTWithInvalidNumberPatternAndSearchPattern() throws Exception {
        // Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        performPostAndExpectStatus("/accounts", bankAccountsFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
        // Case 2
        bankAccountsFilterDto.setNumberPattern("BYby");
        bankAccountsFilterDto.setCustomerPattern("Sergeev2");
        performPostAndExpectStatus("/accounts", bankAccountsFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
    }

}