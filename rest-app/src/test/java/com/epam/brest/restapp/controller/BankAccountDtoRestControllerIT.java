package com.epam.brest.restapp.controller;

import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoRestControllerIT extends RestControllerTestBasic {

    public static final String ACCOUNTS_ENDPOINT = "/accounts";
    public static final String CUSTOMER_SEARCH_PATTERN_IS_INCORRECT = "Customer search pattern is incorrect!";
    public static final String ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT = "Account number search pattern is incorrect!";

    private final BankAccountDtoService bankAccountDtoService;

    private List<BankAccountDto> accounts;
    private BankAccountDto firstAccount;
    private BankAccountDto lastAccount;
    private int lastAccountPosition;

    public BankAccountDtoRestControllerIT(ObjectMapper objectMapper,
                                          MockMvc mockMvc,
                                          BankAccountDtoService bankAccountDtoServiceImpl) {
        super(mockMvc, objectMapper);
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
    }

    @BeforeEach
    void setup(){
        accounts = bankAccountDtoService.getAllWithTotalCards();
        firstAccount = accounts.get(0);
        lastAccountPosition = accounts.size() - 1;
        lastAccount = accounts.get(lastAccountPosition);
    }

    @Test
    void accountsGET() throws Exception {
        performGetAndExpectStatusOk(ACCOUNTS_ENDPOINT)
               .andExpect(jsonPath("$.size()", is(accounts.size())))
               .andExpect(jsonPath("$[0].id", is(firstAccount.getId())))
               .andExpect(jsonPath("$[" + lastAccountPosition + "].id", is(lastAccount.getId())));
    }

    @Test
    void accountsPOST() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern("BY");
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(firstAccount.getId())))
                .andExpect(jsonPath("$[" + lastAccountPosition + "].id", is(lastAccount.getId())));
        // Case 2
        bankAccountFilterDto.setNumberPattern(firstAccount.getNumber()
                                                          .substring(0, 10));
        bankAccountFilterDto.setCustomerPattern(firstAccount.getCustomer());
        accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(accounts.size())))
                .andExpect(jsonPath("$[0].id", is(firstAccount.getId())));
    }

    @Test
    void accountsPOSTWithNoSearchResults() throws Exception {
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        bankAccountFilterDto.setNumberPattern(firstAccount.getNumber()
                                                          .substring(0, 10)
                                                          .replaceFirst("\\d", "A"));
        bankAccountFilterDto.setCustomerPattern(accounts.get(0).getCustomer() + "off");
        performPostAndExpectStatusOk(ACCOUNTS_ENDPOINT, bankAccountFilterDto)
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void accountsPOSTWithInvalidNumberAndSearchPatterns() throws Exception {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        performPostAndExpectStatus(ACCOUNTS_ENDPOINT, bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
        // Case 2
        bankAccountFilterDto.setNumberPattern(firstAccount.getNumber()
                                                          .substring(0, 10)
                                                          .replaceAll("\\d", "#"));
        bankAccountFilterDto.setCustomerPattern(firstAccount.getCustomer() + "123");
        performPostAndExpectStatus(ACCOUNTS_ENDPOINT, bankAccountFilterDto, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.customerPattern").value(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.numberPattern").value(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT));
    }

}