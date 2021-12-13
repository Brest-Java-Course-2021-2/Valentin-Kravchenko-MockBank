package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.BankAccountDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountDtoControllerTest extends RestControllerTestConfiguration {

    private final MockMvc mockMvc;
    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoControllerTest(@Autowired ObjectMapper objectMapper,
                                        @Autowired MockMvc mockMvc,
                                        @Autowired BankAccountDtoService bankAccountDtoService) {
        super(objectMapper);
        this.mockMvc = mockMvc;
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @Test
    void accountsGET() throws Exception {
        List<BankAccountDto> cards = bankAccountDtoService.getAllWithTotalCards();
        int lastIdx = cards.size() - 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", is(cards.size())))
               .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
               .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
    }

    @Test
    void accountsPOST() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setCustomerPattern("");
        bankAccountFilterDto.setNumberPattern("BY");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        int lastIdx = accounts.size() - 1;
        mockMvc.perform(doPost("/accounts", bankAccountFilterDto))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(accounts.get(lastIdx).getId())));
        // Case 2
        String numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        mockMvc.perform(doPost("/accounts", bankAccountFilterDto))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(accounts.get(0).getId())));

    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "EEEEE";
        String customerPattern = "Sergeenko";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        mockMvc.perform(doPost("/accounts", bankAccountFilterDto))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void accountsPOSTFailed() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        mockMvc.perform(doPost("/accounts", bankAccountFilterDto))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value("Customer search pattern is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value("Account number search pattern is incorrect!"));
        // Case 2
        bankAccountFilterDto.setNumberPattern("");
        bankAccountFilterDto.setCustomerPattern("");
        mockMvc.perform(doPost("/accounts", bankAccountFilterDto))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value("Customer search pattern is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value("Account number search pattern is incorrect!"));
    }

}