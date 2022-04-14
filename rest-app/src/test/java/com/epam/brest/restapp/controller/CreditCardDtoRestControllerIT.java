package com.epam.brest.restapp.controller;

import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.service.api.CreditCardDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardDtoRestControllerIT extends RestControllerTestBasic {

    public static final String CARDS_ENDPOINT = "/cards";
    public static final String FROM_DATE_VALUE = "fromDateValue";
    public static final String TO_DATE_VALUE = "toDateValue";
    public static final String FROM_DATE_TEST_VALUE = "05/2022";
    public static final String TO_DATE_TEST_VALUE = "07/2022";
    public static final String START_DATE_FORMAT_IS_INCORRECT = "Start date format is incorrect!";
    public static final String END_DATE_FORMAT_IS_INCORRECT = "End date format is incorrect!";
    public static final String START_DATE_IS_WRONG = "Start date is wrong!";
    public static final String END_DATE_IS_WRONG = "End date is wrong!";

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
        int lastPosition = cards.size() - 1;
        performGetAndExpectStatusOk(CARDS_ENDPOINT)
               .andExpect(jsonPath("$.size()", is(cards.size())))
               .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
               .andExpect(jsonPath("$[" + lastPosition + "].id", is(cards.get(lastPosition).getId())));
    }

    @Test
    void cardsPOST() throws Exception {
        //Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDateValue(FROM_DATE_TEST_VALUE);
        creditCardFilterDto.setToDateValue(TO_DATE_TEST_VALUE);
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        int lastPosition = cards.size() - 1;
        Map<String, Object> body = new HashMap<>();
        body.put(FROM_DATE_VALUE, FROM_DATE_TEST_VALUE);
        body.put(TO_DATE_VALUE, TO_DATE_TEST_VALUE);
        performPostAndExpectStatusOk(CARDS_ENDPOINT, body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastPosition + "].id", is(cards.get(lastPosition).getId())));
        //Case 2
        creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDateValue(FROM_DATE_TEST_VALUE);
        cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        lastPosition = cards.size() - 1;
        body.clear();
        body.put(FROM_DATE_VALUE, FROM_DATE_TEST_VALUE);
        performPostAndExpectStatusOk(CARDS_ENDPOINT, body)
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].id", is(cards.get(0).getId())))
                .andExpect(jsonPath("$[" + lastPosition + "].id", is(cards.get(lastPosition).getId())));
    }

    @Test
    void cardsPOSTWithNoSearchResults() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(TO_DATE_VALUE, "07/2000");
        performPostAndExpectStatusOk(CARDS_ENDPOINT, body)
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void cardsPOSTWithInvalidFromAndToDateValues() throws Exception {
        // Case 1
        Map<String, Object> body = new HashMap<>();
        body.put(FROM_DATE_VALUE, FROM_DATE_TEST_VALUE);
        body.put(TO_DATE_VALUE, FROM_DATE_TEST_VALUE);
        performPostAndExpectStatus(CARDS_ENDPOINT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.fromDateValue").value(START_DATE_IS_WRONG))
                .andExpect(jsonPath("$.validationErrors.toDateValue").value(END_DATE_IS_WRONG));
        // Case 2
        body.put(FROM_DATE_VALUE, "00/2022");
        body.put(TO_DATE_VALUE, "07.2022");
        performPostAndExpectStatus(CARDS_ENDPOINT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.fromDateValue").value(START_DATE_FORMAT_IS_INCORRECT))
                .andExpect(jsonPath("$.validationErrors.toDateValue").value(END_DATE_FORMAT_IS_INCORRECT));
        // Case 3
        body.clear();
        performPostAndExpectStatus(CARDS_ENDPOINT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.fromDateValue").value(START_DATE_IS_WRONG))
                .andExpect(jsonPath("$.validationErrors.toDateValue").value(END_DATE_IS_WRONG));
    }

}