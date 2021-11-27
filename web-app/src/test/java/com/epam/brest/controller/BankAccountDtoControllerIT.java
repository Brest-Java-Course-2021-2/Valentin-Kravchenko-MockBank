package com.epam.brest.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.service.BankAccountDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountDtoControllerIT extends BasicControllerTest {

    private final BankAccountDtoService bankAccountDtoService;

    private List<BankAccountDto> accounts;

    public BankAccountDtoControllerIT(@Autowired BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @BeforeEach
    void setup(){
        accounts = bankAccountDtoService.getAllWithTotalCards();
    }

    @Test
    void accounts() throws Exception {
        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("accounts"))
               .andExpect(model().attribute("accounts", accounts));
    }

}