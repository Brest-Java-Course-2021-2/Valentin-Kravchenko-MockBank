package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditCardDtoServiceRestTest extends ServiceRestTestBasic {

    public static final String DATES_MUST_BE_DIFFERENT = "Dates must be different!";
    public static final String RANGE_START_DATE_FORMAT_IS_INCORRECT = "Range start date format is incorrect!";
    public static final String RANGE_END_DATE_FORMAT_IS_INCORRECT = "Range end date format is incorrect!";

    public CreditCardDtoServiceRestTest(@Autowired WebTestClient webTestClient) {
        super(webTestClient);
    }

    @Test
    void getAllWithAccountNumber() {
        getAndExpectStatusOk("/cards").expectBodyList(CreditCardDto.class).value(hasSize(greaterThan(0)));
    }

    @Test
    void getAllWithAccountNumberByFilter() {
        // Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("05/2022");
        creditCardDateRangeDto.setValueToDate("07/2022");
        postAndExpectStatusOk("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectBodyList(CreditCardDto.class)
                .consumeWith(result -> {
                    CreditCardDto firstCreditCardDto = result.getResponseBody().get(0);
                    CreditCardDto lastCreditCardDto = result.getResponseBody().get(result.getResponseBody().size() - 1);
                    assertEquals(firstCreditCardDto.getExpirationDate().getMonthValue(), 5);
                    assertEquals(lastCreditCardDto.getExpirationDate().getMonthValue(), 7);
                    assertEquals(firstCreditCardDto.getExpirationDate().getYear(), 2022);
                    assertEquals(lastCreditCardDto.getExpirationDate().getYear(), 2022);
                });
        // Case 2
        creditCardDateRangeDto.setValueFromDate("05/2022");
        creditCardDateRangeDto.setValueToDate("");
        postAndExpectStatusOk("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectBodyList(CreditCardDto.class)
                .consumeWith(result -> {
                    CreditCardDto firstCreditCardDto = result.getResponseBody().get(0);
                    assertEquals(firstCreditCardDto.getExpirationDate().getMonthValue(), 5);
                    assertEquals(firstCreditCardDto.getExpirationDate().getYear(), 2022);
                });
        // Case 3
        creditCardDateRangeDto.setValueFromDate("");
        creditCardDateRangeDto.setValueToDate("07/2022");
        postAndExpectStatusOk("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectBodyList(CreditCardDto.class)
                .consumeWith(result -> {
                    CreditCardDto lastCreditCardDto = result.getResponseBody().get(result.getResponseBody().size() - 1);
                    assertEquals(lastCreditCardDto.getExpirationDate().getMonthValue(), 7);
                    assertEquals(lastCreditCardDto.getExpirationDate().getYear(), 2022);
                });
    }

    @Test
    void getAllWithAccountNumberByFilterWithInvalidValueFromDateAndValueToDate() {
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("07/2022");
        creditCardDateRangeDto.setValueToDate("07/2022");
        postAndExchange("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.validationErrors.valueFromDate").isEqualTo(DATES_MUST_BE_DIFFERENT)
                .jsonPath("$.validationErrors.valueToDate").isEqualTo(DATES_MUST_BE_DIFFERENT);
        // Case 2
        creditCardDateRangeDto.setValueFromDate("00/2022");
        creditCardDateRangeDto.setValueToDate("07.2022");
        postAndExchange("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.validationErrors.valueFromDate").isEqualTo(RANGE_START_DATE_FORMAT_IS_INCORRECT)
                .jsonPath("$.validationErrors.valueToDate").isEqualTo(RANGE_END_DATE_FORMAT_IS_INCORRECT);
        // Case 3
        creditCardDateRangeDto.setValueFromDate("");
        creditCardDateRangeDto.setValueToDate("");
        postAndExchange("/cards", Mono.just(creditCardDateRangeDto), CreditCardDateRangeDto.class)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.validationErrors.valueFromDate").isEqualTo(RANGE_START_DATE_FORMAT_IS_INCORRECT)
                .jsonPath("$.validationErrors.valueToDate").isEqualTo(RANGE_END_DATE_FORMAT_IS_INCORRECT);
    }

}