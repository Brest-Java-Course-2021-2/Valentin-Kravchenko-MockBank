package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditCardServiceRestIT extends ServiceRestTestBasic {

    public static final String CARD_DEPOSIT_ENDPOINT = "/card/deposit";
    public static final String CARD_TRANSFER_ENDPOINT = "/card/transfer";
    public static final String CARD_1_ENDPOINT = "/card/1";
    public static final String CARD_ENDPOINT = "/card";

    public CreditCardServiceRestIT(@Autowired WebTestClient webTestClient) {
        super(webTestClient);
    }

    @Test
    void create() {
        Integer accountId = 1;
        postAndExpectStatusOk(CARD_ENDPOINT, accountId)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getAccountId(), accountId));
    }

    @Test
    void remove() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk(CARD_ENDPOINT, accountId)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        deleteAndExpectStatusOk("/card/" + createdCreditCard.getId())
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), createdCreditCard.getId()));

    }

    @Test
    void failedRemove() {
        deleteAndExchange(CARD_1_ENDPOINT)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", notNullValue());
    }

    @Test
    void deposit() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk("/card", accountId)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(createdCreditCard.getNumber());
        creditCardTransactionDto.setValueSumOfMoney("1560,35");
        creditCardTransactionDto.setLocale(new Locale("ru"));
        postAndExpectStatusOk(CARD_DEPOSIT_ENDPOINT, creditCardTransactionDto)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getBalance(), new BigDecimal("1560.35")));
    }

    @Test
    void transfer() {
        Integer accountId = 1;
        CreditCard sourceCreditCard = postAndExpectStatusOk("/card", accountId)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCard targetCreditCard = postAndExpectStatusOk("/card", accountId)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setValueSumOfMoney("1000");
        creditCardTransactionDto.setLocale(new Locale("ru"));
        postAndExpectStatusOk(CARD_DEPOSIT_ENDPOINT, creditCardTransactionDto);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        postAndExpectStatusOk(CARD_TRANSFER_ENDPOINT, creditCardTransactionDto)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getBalance().intValue(), 0));
    }

    @Test
    void failedTransfer() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk("/card", accountId)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCard creditCardFromDb = getAndExpectStatusOk("/card/1")
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(createdCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        creditCardTransactionDto.setValueSumOfMoney("1000");
        creditCardTransactionDto.setLocale(new Locale("ru"));
        postAndExchange(CARD_TRANSFER_ENDPOINT, creditCardTransactionDto)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", notNullValue());
    }

    @Test
    void getById() {
        getAndExpectStatusOk(CARD_1_ENDPOINT)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), 1));
    }

}