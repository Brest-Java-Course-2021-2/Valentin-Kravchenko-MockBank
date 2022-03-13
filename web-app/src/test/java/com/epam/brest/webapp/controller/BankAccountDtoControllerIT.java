package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.epam.brest.webapp.constant.ControllerConstant.ACCOUNTS;
import static com.epam.brest.webapp.constant.ControllerConstant.ERROR;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class BankAccountDtoControllerIT extends ControllerTestBasic {

    public static final String FILTER = "filter";
    public static final String NUMBER_PATTERN = "numberPattern";
    public static final String CUSTOMER_PATTERN = "customerPattern";

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoControllerIT(@Autowired MockMvc mockMvc,
                                      @Autowired BankAccountDtoService bankAccountDtoServiceRest) {
        super(mockMvc);
        this.bankAccountDtoService = bankAccountDtoServiceRest;
    }

    @Test
    void accountsGET() throws Exception {
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        performGetAndExpectStatusOk(ACCOUNTS_ENDPOINT, ACCOUNTS).andExpect(model().attribute(ACCOUNTS, accounts));
    }

    @Test
    void accountsPOST() throws Exception {
        // Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String numberPattern = "BY";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(NUMBER_PATTERN, numberPattern);
        params.add(CUSTOMER_PATTERN, EMPTY);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, params, ACCOUNTS)
                .andExpect(model().attribute(ACCOUNTS, accounts));
        // Case 2
        params.clear();
        numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        bankAccountsFilterDto.setCustomerPattern(customerPattern);
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        params.add(NUMBER_PATTERN, numberPattern);
        params.add(CUSTOMER_PATTERN, customerPattern);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, params, ACCOUNTS)
                .andExpect(model().attribute(ACCOUNTS, accounts));
    }

    @Test
    void accountsPOSTThereAreNoSearchResults() throws Exception {
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String numberPattern = "EEEEE";
        String customerPattern = "Sergeenko";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        bankAccountsFilterDto.setCustomerPattern(customerPattern);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountsFilterDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(NUMBER_PATTERN, numberPattern);
        params.add(CUSTOMER_PATTERN, customerPattern);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, params, ACCOUNTS)
                .andExpect(model().attributeExists(ERROR))
                .andExpect(model().attribute(ACCOUNTS, accounts));

    }

    @Test
    void accountsPOSTWithInvalidNumberPatternAndSearchPattern() throws Exception {
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(NUMBER_PATTERN, numberPattern);
        params.add(CUSTOMER_PATTERN, customerPattern);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, params, ACCOUNTS)
                .andExpect(model().attribute(FILTER, hasProperty(NUMBER_PATTERN, is(numberPattern))))
                .andExpect(model().attribute(FILTER, hasProperty(CUSTOMER_PATTERN, is(customerPattern))));
    }

}