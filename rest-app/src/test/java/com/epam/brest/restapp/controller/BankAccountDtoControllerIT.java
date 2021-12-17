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

class BankAccountDtoControllerIT extends RestControllerTestBasic {

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoControllerIT(@Autowired ObjectMapper objectMapper,
                                      @Autowired MockMvc mockMvc,
                                      @Autowired BankAccountDtoService bankAccountDtoService) {
        super(mockMvc, objectMapper);
        this.bankAccountDtoService = bankAccountDtoService;
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
        bankAccountFilterDto.setCustomerPattern("");
        bankAccountFilterDto.setNumberPattern("BY");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        int lastIdx = accounts.size() - 1;
        performPostAndExpectStatusOk("/accounts", bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(accounts.get(lastIdx).getId())));
        // Case 2
        bankAccountFilterDto.setNumberPattern("TQ99IK");
        bankAccountFilterDto.setCustomerPattern("Sergeev");
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        performPostAndExpectStatusOk("/accounts", bankAccountFilterDto)
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
        performPostAndExpectStatusOk("/accounts", bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void accountsPOSTWithInvalidNumberPatternAndSearchPattern() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        performPostAndExpectStatus("/accounts", bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value("Customer search pattern is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value("Account number search pattern is incorrect!"));
        // Case 2
        bankAccountFilterDto.setNumberPattern("");
        bankAccountFilterDto.setCustomerPattern("");
        performPostAndExpectStatus("/accounts", bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value("Customer search pattern is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value("Account number search pattern is incorrect!"));
    }

}