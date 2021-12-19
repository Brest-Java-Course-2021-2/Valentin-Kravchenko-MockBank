package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class CreditCardDtoControllerIT extends ControllerTestBasic {

    public static final String VALUE_FROM_DATE = "valueFromDate";
    public static final String VALUE_TO_DATE = "valueToDate";

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoControllerIT(@Autowired MockMvc mockMvc,
                                     @Autowired CreditCardDtoService creditCardDtoServiceRest) {
        super(mockMvc);
        this.creditCardDtoService = creditCardDtoServiceRest;
    }

    @Test
    void cardsGET() throws Exception {
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        performGetAndExpectStatusOk(CARDS_ENDPOINT, CARDS)
                .andExpect(model().attribute(CARDS, cards));
    }

    @Test
    void cardsPOST() throws Exception {
        //Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueFromDate("05/2022");
        creditCardDateRangeDto.setValueToDate("07/2022");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(VALUE_FROM_DATE, "05/2022");
        params.add(VALUE_TO_DATE, "07/2022");
        performPostAndExpectStatusOk(CARDS_ENDPOINT, params, CARDS)
                .andExpect(model().attribute(CARDS, cards));
        //Case 2
        params.clear();
        creditCardDateRangeDto.setValueToDate(null);
        cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        params.add(VALUE_FROM_DATE, "05/2022");
        params.add(VALUE_TO_DATE, "");
        performPostAndExpectStatusOk(CARDS_ENDPOINT, params, CARDS)
                .andExpect(model().attribute(CARDS, cards));
    }

    @Test
    void cardsPOSTThereAreNoSearchResults() throws Exception {
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setValueToDate("07/2000");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(VALUE_FROM_DATE, "");
        params.add(VALUE_TO_DATE, "07/2000");
        performPostAndExpectStatusOk(CARDS_ENDPOINT, params, CARDS)
                .andExpect(model().attributeExists(ERROR))
                .andExpect(model().attribute(CARDS, cards));
    }

    @Test
    void cardsPOSTWithInvalidValueFromDateAndValueToDate() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(VALUE_FROM_DATE, "00/2022");
        params.add(VALUE_TO_DATE, "07.2022");
        performPostAndExpectStatusOk(CARDS_ENDPOINT, params, CARDS)
                .andExpect(model().attribute(FILTER, hasProperty(VALUE_FROM_DATE, is("00/2022"))))
                .andExpect(model().attribute(FILTER, hasProperty(VALUE_TO_DATE, is("07.2022"))));
    }

}