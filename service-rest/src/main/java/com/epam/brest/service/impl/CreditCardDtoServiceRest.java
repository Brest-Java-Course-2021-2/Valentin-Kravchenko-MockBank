package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.CreditCardDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class CreditCardDtoServiceRest extends ServiceRestBasic implements CreditCardDtoService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoServiceRest.class);

    private final String endpoint;
    private final ParameterizedTypeReference<List<CreditCardDto>> parameterizedTypeRef;

    public CreditCardDtoServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        LOGGER.debug("getAllWithAccountNumber(endpoint={})", endpoint);
        return getBlock(endpoint, parameterizedTypeRef);
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber(CreditCardDateRangeDto creditCardDateRangeDto) {
        LOGGER.debug("getAllWithTotalCards(endpoint={}, creditCardDateRangeDto={})", endpoint, creditCardDateRangeDto);
        return postBlock(endpoint, creditCardDateRangeDto, parameterizedTypeRef);
    }

}
