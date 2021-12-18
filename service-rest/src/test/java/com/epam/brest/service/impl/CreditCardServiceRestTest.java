package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditCardServiceRestTest extends ServiceRestTestBasic {

    public CreditCardServiceRestTest(@Autowired WebTestClient webTestClient) {
        super(webTestClient);
    }

    @Test
    void create() {
        Integer accountId = 1;
        postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getAccountId(), accountId));
    }

    @Test
    void remove() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        deleteAndExpectStatusOk("/card/" + createdCreditCard.getId())
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), createdCreditCard.getId()));

    }

    @Test
    void failedRemove() {
        deleteAndExchange("/card/1")
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", notNullValue());
    }

    @Test
    void deposit() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(createdCreditCard.getNumber());
        creditCardTransactionDto.setValueSumOfMoney("1560,35");
        creditCardTransactionDto.setLocale(new Locale("ru"));
        postAndExpectStatusOk("/card/" + createdCreditCard.getId() + "/deposit",
                              Mono.just(creditCardTransactionDto), CreditCardTransactionDto.class)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getBalance(), new BigDecimal("1560.35")));
    }

    @Test
    void transfer() {
        Integer accountId = 1;
        CreditCard sourceCreditCard = postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCard targetCreditCard = postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
                                            .expectBody(CreditCard.class)
                                            .returnResult()
                                            .getResponseBody();
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setValueSumOfMoney("1000");
        creditCardTransactionDto.setLocale(new Locale("ru"));
        postAndExpectStatusOk("/card/" + sourceCreditCard.getId() + "/deposit",
                              Mono.just(creditCardTransactionDto), CreditCardTransactionDto.class);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        postAndExpectStatusOk("/card/" + sourceCreditCard.getId() + "/transfer",
                              Mono.just(creditCardTransactionDto), CreditCardTransactionDto.class)
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getBalance().intValue(), 0));
    }

    @Test
    void failedTransfer() {
        Integer accountId = 1;
        CreditCard createdCreditCard = postAndExpectStatusOk("/card", Mono.just(accountId), Integer.class)
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
        postAndExchange("/card/" + createdCreditCard.getId() + "/transfer",
                        Mono.just(creditCardTransactionDto), CreditCardTransactionDto.class)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", notNullValue());
    }

    @Test
    void getById() {
        getAndExpectStatusOk("/card/1")
                .expectBody(CreditCard.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), 1));
    }

}