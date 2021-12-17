package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreditCardDtoControllerIT extends RestControllerTestBasic {

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoControllerIT(@Autowired ObjectMapper objectMapper,
                                     @Autowired MockMvc mockMvc,
                                     @Autowired CreditCardDtoService creditCardDtoService) {
        super(mockMvc, objectMapper);
        this.creditCardDtoService = creditCardDtoService;
    }

    @Test
    void cardsGET() throws Exception {
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        int lastIdx = cards.size() - 1;
        performGetAndExpectStatusOk("/cards")
               .andExpect(jsonPath("$.size()", is(cards.size())))
               .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
               .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
    }

    @Test
    void cardsPOST() throws Exception {
        //Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setFromDate(LocalDate.parse("2022-05-31"));
        creditCardDateRangeDto.setToDate(LocalDate.parse("2022-07-31"));
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        int lastIdx = cards.size() - 1;
        Map<String, Object> body = new HashMap<>();
        body.put("valueFromDate", "05/2022");
        body.put("valueToDate", "07/2022");
        performPostAndExpectStatusOk("/cards", body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
        //Case 2
        creditCardDateRangeDto.setToDate(null);
        cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        lastIdx = cards.size() - 1;
        body.put("valueToDate", "");
        performPostAndExpectStatusOk("/cards", body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
    }

    @Test
    void cardsPOSTThereAreNoSearchResults() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("valueFromDate", "");
        body.put("valueToDate", "07/2000");
        performPostAndExpectStatusOk("/cards", body).andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void cardsPOSTWithInvalidValueFromDateAndValueToDate() throws Exception {
        // Case 1
        Map<String, Object> body = new HashMap<>();
        body.put("valueFromDate", "07/2022");
        body.put("valueToDate", "07/2022");
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value("Dates must be different!"))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value("Dates must be different!"));
        // Case 2
        body.put("valueFromDate", "00/2022");
        body.put("valueToDate", "07.2022");
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value("Range start date format is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value("Range end date format is incorrect!"));
        // Case 3
        body.put("valueFromDate", "");
        body.put("valueToDate", "");
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value("Range start date format is incorrect!"))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value("Range end date format is incorrect!"));
    }

}