package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountDtoControllerIT extends ControllerTestConfiguration {

    private final MockMvc mockMvc;
    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoControllerIT(@Autowired MockMvc mockMvc,
                                      @Autowired BankAccountDtoService bankAccountDtoService) {
        this.mockMvc = mockMvc;
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @Test
    void accountsGET() throws Exception {
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("accounts"))
               .andExpect(model().attribute("accounts", accounts));
    }

    @Test
    void accountsPOST() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("numberPattern", numberPattern);
        params.add("customerPattern", customerPattern);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("accounts", accounts));
        // Case 2
        numberPattern = "BY";
        customerPattern = "";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        params.clear();
        params.add("numberPattern", numberPattern);
        params.add("customerPattern", customerPattern);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("accounts", accounts));
    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "EEEEE";
        String customerPattern = "Sergeenko";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("numberPattern", numberPattern);
        params.add("customerPattern", customerPattern);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("accounts", accounts));

    }

    @Test
    void accountsPOSTFailed() throws Exception {
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("numberPattern", numberPattern);
        params.add("customerPattern", customerPattern);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("filter", hasProperty("numberPattern", is("BYby"))))
                .andExpect(model().attribute("filter", hasProperty("customerPattern", is("Sergeev2"))));
    }

}