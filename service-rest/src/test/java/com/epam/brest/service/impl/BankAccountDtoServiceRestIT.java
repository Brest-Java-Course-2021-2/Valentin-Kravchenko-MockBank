package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
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
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        postAndExpectStatusOk("/accounts", bankAccountFilterDto)
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().get(0).getCustomer().contains(customerPattern)));
        //case 2
        bankAccountFilterDto.setNumberPattern(null);
        postAndExpectStatusOk("/accounts", bankAccountFilterDto)
                     .expectBody().jsonPath("$.size()", is(1));
        //case 3
        bankAccountFilterDto.setNumberPattern("BY");
        bankAccountFilterDto.setCustomerPattern(null);
        postAndExpectStatusOk("/accounts", bankAccountFilterDto)
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY"))));
    }

    @Test
    void getAllWithTotalCardsByFilterWithInvalidNumberPatternAndSearchPattern() {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        postAndExchange("/accounts", bankAccountFilterDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.validationErrors.customerPattern").isEqualTo(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT)
                .jsonPath("$.validationErrors.numberPattern").isEqualTo(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT);
        // Case 2
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        postAndExchange("/accounts", bankAccountFilterDto)
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.validationErrors.customerPattern").isEqualTo(CUSTOMER_SEARCH_PATTERN_IS_INCORRECT)
                     .jsonPath("$.validationErrors.numberPattern").isEqualTo(ACCOUNT_NUMBER_SEARCH_PATTERN_IS_INCORRECT);
    }

}