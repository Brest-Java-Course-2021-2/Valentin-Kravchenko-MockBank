package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountDtoServiceRestTest extends ServiceRestTestBasic {

    private final WebTestClient webTestClient;

    public BankAccountDtoServiceRestTest(@Autowired WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void getAllWithTotalCards(){
        WebTestClient.ListBodySpec<BankAccountDto> bankAccountDtoListBodySpec =
                webTestClient.get()
                             .uri("/accounts")
                             .exchange()
                             .expectHeader().valueEquals("Content-Type", "application/json")
                             .expectStatus().isOk()
                             .expectBodyList(BankAccountDto.class)
                             .value(notNullValue())
                             .value(hasSize(greaterThan(0)));
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "TQ99IK";
        String customerPattern = "Sergeev";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        webTestClient.post()
                     .uri("/accounts")
                     .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                     .exchange()
                     .expectHeader().valueEquals("Content-Type", "application/json")
                     .expectStatus().isOk()
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().get(0).getCustomer().contains(customerPattern)));
        //case 2
        bankAccountFilterDto.setNumberPattern("");
        webTestClient.post()
                     .uri("/accounts")
                     .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                     .exchange()
                     .expectHeader().valueEquals("Content-Type", "application/json")
                     .expectStatus().isOk()
                     .expectBody().jsonPath("$.size()", is(1));
        //case 3
        bankAccountFilterDto.setNumberPattern("BY");
        bankAccountFilterDto.setCustomerPattern("");
        webTestClient.post()
                     .uri("/accounts")
                     .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                     .exchange()
                     .expectHeader().valueEquals("Content-Type", "application/json")
                     .expectStatus().isOk()
                     .expectBodyList(BankAccountDto.class)
                     .consumeWith(result -> assertTrue(result.getResponseBody().stream().map(BankAccountDto::getNumber).allMatch(n -> n.contains("BY"))));
    }

    @Test
    void getAllWithTotalCardsByFilterFailed() {
        // Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String numberPattern = "BYby";
        String customerPattern = "Sergeev2";
        bankAccountFilterDto.setNumberPattern(numberPattern);
        bankAccountFilterDto.setCustomerPattern(customerPattern);
        webTestClient.post()
                     .uri("/accounts")
                     .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                     .exchange()
                     .expectHeader().valueEquals("Content-Type", "application/json")
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.validationErrors.customerPattern").isEqualTo("Customer search pattern is incorrect!")
                     .jsonPath("$.validationErrors.numberPattern").isEqualTo("Account number search pattern is incorrect!");
        // Case 2
        bankAccountFilterDto.setNumberPattern("");
        bankAccountFilterDto.setCustomerPattern("");
        webTestClient.post()
                     .uri("/accounts")
                     .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                     .exchange()
                     .expectHeader().valueEquals("Content-Type", "application/json")
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.validationErrors.customerPattern").isEqualTo("Customer search pattern is incorrect!")
                     .jsonPath("$.validationErrors.numberPattern").isEqualTo("Account number search pattern is incorrect!");
    }

}