package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BankAccountDtoRestControllerIT extends RestControllerTestBasic {

    public static final String CUSTOMER_SEARCH_PATTERN_IS_INCORRECT = "Customer search pattern is incorrect!";
    public static final String ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT = "Account number search pattern is incorrect!";
    public static final String ACCOUNTS_ENDPOINT = "/accounts";

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoRestControllerIT(@Autowired ObjectMapper objectMapper,
                                          @Autowired MockMvc mockMvc,
                                          @Autowired BankAccountDtoService bankAccountDtoServiceImpl) {
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
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("BY");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        int lastIdx = accounts.size() - 1;
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())))
                    .andExpect(jsonPath("$[" + lastIdx + "].id", is(accounts.get(lastIdx).getId())));
        // Case 2
        bankAccountFilterDto.setNumberPattern("TQ99IK");
        bankAccountFilterDto.setCustomerPattern("Sergeev");
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())));
    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "EEEEE";
        String customerPattern = "Sergeenko";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void accountsPOSTWithInvalidNumberPatternAndSearchPattern() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        performPostAndExpectStatus("/accounts", bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
        // Case 2
        bankAccountFilterDto.setNumberPattern("BYby");
        bankAccountFilterDto.setCustomerPattern("Sergeev2");
        performPostAndExpectStatus("/accounts", bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
    }

}