package com.epam.brest.restapp.controller;

import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.service.api.CreditCardDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardDtoRestControllerIT extends RestControllerTestBasic {

    public static final String DATES_MUST_BE_DIFFERENT = "Dates must be different!";
    public static final String RANGE_START_DATE_FORMAT_IS_INCORRECT = "Range start date format is incorrect!";
    public static final String RANGE_END_DATE_FORMAT_IS_INCORRECT = "Range end date format is incorrect!";
    public static final String VALUE_FROM_DATE = "valueFromDate";
    public static final String VALUE_TO_DATE = "valueToDate";

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoRestControllerIT(ObjectMapper objectMapper,
                                         MockMvc mockMvc,
                                         CreditCardDtoService creditCardDtoServiceImpl) {
        super(mockMvc, objectMapper);
        this.creditCardDtoService = creditCardDtoServiceImpl;
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
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDate(LocalDate.parse("2022-05-31"));
        creditCardFilterDto.setToDate(LocalDate.parse("2022-07-31"));
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        int lastIdx = cards.size() - 1;
        Map<String, Object> body = new HashMap<>();
        body.put(VALUE_FROM_DATE, "05/2022");
        body.put(VALUE_TO_DATE, "07/2022");
        performPostAndExpectStatusOk("/cards", body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
        //Case 2
        creditCardFilterDto.setToDate(null);
        cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        lastIdx = cards.size() - 1;
        body.remove(VALUE_TO_DATE);
        performPostAndExpectStatusOk("/cards", body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastIdx + "].id", is(cards.get(lastIdx).getId())));
    }

    @Test
    void cardsPOSTThereAreNoSearchResults() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(VALUE_TO_DATE, "07/2000");
        performPostAndExpectStatusOk("/cards", body).andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void cardsPOSTWithInvalidValueFromDateAndValueToDate() throws Exception {
        // Case 1
        Map<String, Object> body = new HashMap<>();
        body.put(VALUE_FROM_DATE, "07/2022");
        body.put(VALUE_TO_DATE, "07/2022");
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value(DATES_MUST_BE_DIFFERENT))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value(DATES_MUST_BE_DIFFERENT));
        // Case 2
        body.put(VALUE_FROM_DATE, "00/2022");
        body.put(VALUE_TO_DATE, "07.2022");
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value(RANGE_START_DATE_FORMAT_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value(RANGE_END_DATE_FORMAT_IS_INCORRECT));
        // Case 3
        body.clear();
        performPostAndExpectStatus("/cards", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueFromDate").value(RANGE_START_DATE_FORMAT_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.valueToDate").value(RANGE_END_DATE_FORMAT_IS_INCORRECT));
    }

}