package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountDtoControllerIT extends BasicControllerTest {

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoControllerIT(@Autowired BankAccountDtoService bankAccountDtoService) {
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
    void accountsPOSTThereAreSearchResults() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String number = "TQ99IK";
        String customer = "Sergeev";
        bankAccountFilterDto.setNumber(number);
        bankAccountFilterDto.setCustomer(customer);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("number", number);
        params.add("customer", customer);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("accounts", accounts));
        // Case 2
        number = "BY";
        customer = "";
        bankAccountFilterDto.setNumber(number);
        bankAccountFilterDto.setCustomer(customer);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        params.clear();
        params.add("number", number);
        params.add("customer", customer);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("accounts", accounts));
    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String number = "EEEEE";
        String customer = "Sergeenko";
        bankAccountFilterDto.setNumber(number);
        bankAccountFilterDto.setCustomer(customer);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("number", number);
        params.add("customer", customer);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("accounts", accounts));

    }

    @Test
    void accountsPOSTFailed() throws Exception {
        String number = "BYby";
        String customer = "Sergeev2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("number", number);
        params.add("customer", customer);
        mockMvc.perform(post("/accounts").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("accounts"))
                .andExpect(model().attribute("filter", hasProperty("number", is("BYby"))))
                .andExpect(model().attribute("filter", hasProperty("customer", is("Sergeev2"))));
    }

}