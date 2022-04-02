package com.epam.brest.service.impl;

import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountServiceRestIT extends ServiceRestTestBasic {

    public BankAccountServiceRestIT(@Autowired WebTestClient webTestClient) {
        super(webTestClient);
    }

    @Test
    void create() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer");
        postAndExpectStatusOk("/account", bankAccount)
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getCustomer(), "New Customer"));
    }

    @Test
    void update() {
        BankAccount bankAccount = new BankAccount();
        String customer = "New Customer";
        bankAccount.setId(1);
        bankAccount.setCustomer(customer);
        putAndExpectStatusOk("/account", bankAccount)
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getCustomer(), "New Customer"));
    }

    @Test
    void failedUpdate() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1000);
        bankAccount.setCustomer("New Customer");
        putAndExchange("/account", bankAccount)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", containsString("1000"));
    }

    @Test
    void remove() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer");
        BankAccount bankAccountFromDb = postAndExpectStatusOk("/account", bankAccount)
                .expectBody(BankAccount.class)
                .returnResult()
                .getResponseBody();
        deleteAndExpectStatusOk("/account/" + bankAccountFromDb.getId())
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), bankAccountFromDb.getId()));
    }

    @Test
    void failedRemove() {
        deleteAndExchange("/account/1")
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", notNullValue());
    }

    @Test
    void getById() {
        getAndExpectStatusOk("/account/1")
                .expectBody(BankAccount.class)
                .consumeWith(result -> assertEquals(result.getResponseBody().getId(), 1));

    }

    @Test
    void getAllCardsById() {
        getAndExpectStatusOk("/account/1/cards")
                .expectBodyList(CreditCard.class)
                .consumeWith(result -> assertTrue(result.getResponseBody().stream()
                                                                          .map(CreditCard::getAccountId)
                                                                          .allMatch(accountId -> accountId.equals(1))));

    }

}