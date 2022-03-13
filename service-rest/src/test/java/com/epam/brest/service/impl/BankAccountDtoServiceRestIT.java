package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountDtoServiceRestIT extends ServiceRestTestBasic {

    public static final String CUSTOMER_SEARCH_PATTERN_IS_INCORRECT = "Customer search pattern is incorrect!";
    public static final String ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT = "Account number search pattern is incorrect!";

    public BankAccountDtoServiceRestIT(@Autowired WebTestClient webTestClient) {
        super(webTestClient);
    }

    @Test
    void getAllWithTotalCards(){
        getAndExpectStatusOk("/accounts").expectBodyList(BankAccountDto.class).value(hasSize(greaterThan(0)));
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        // Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        bankAccountsFilterDto.setCustomerPattern(customerPattern);
        postAndExpectStatusOk("/accounts", bankAccountsFilterDto)
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().get(0).getCustomer().contains(customerPattern)));
        //case 2
        bankAccountsFilterDto.setNumberPattern(null);
        postAndExpectStatusOk("/accounts", bankAccountsFilterDto)
                     .expectBody().jsonPath("$.size()", is(1));
        //case 3
        bankAccountsFilterDto.setNumberPattern("BY");
        bankAccountsFilterDto.setCustomerPattern(null);
        postAndExpectStatusOk("/accounts", bankAccountsFilterDto)
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY"))));
    }

    @Test
    void getAllWithTotalCardsByFilterWithInvalidNumberPatternAndSearchPattern() {
        // Case 1
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        postAndExchange("/accounts", bankAccountsFilterDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.validationErrors.customerPattern").isEqualTo(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT)
                .jsonPath("$.validationErrors.numberPattern").isEqualTo(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT);
        // Case 2
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        bankAccountsFilterDto.setNumberPattern(numberPattern);
        bankAccountsFilterDto.setCustomerPattern(customerPattern);
        postAndExchange("/accounts", bankAccountsFilterDto)
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.validationErrors.customerPattern").isEqualTo(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT)
                     .jsonPath("$.validationErrors.numberPattern").isEqualTo(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT);
    }

}