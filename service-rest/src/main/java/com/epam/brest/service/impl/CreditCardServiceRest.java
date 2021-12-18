package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class CreditCardServiceRest extends ServiceRestBasic implements CreditCardService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardServiceRest.class);

    private final String endpoint;
    private final ParameterizedTypeReference<CreditCard> parameterizedTypeRef;

    public CreditCardServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
    }

    @Override
    public CreditCard create(Integer accountId) {
        LOGGER.debug("create(endpoint={})", endpoint);
        return webClientPostBlock(endpoint, accountId, parameterizedTypeRef);
    }

    @Override
    public CreditCard delete(Integer id) {
        LOGGER.debug("delete(endpoint={})", endpoint + "/" + id);
        return webClientDeleteBlock(endpoint + "/" + id, parameterizedTypeRef);
    }

    @Override
    public CreditCard deposit(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(endpoint={}, creditCardTransactionDto={})", endpoint, creditCardTransactionDto);
        return webClientPostBlock(endpoint, creditCardTransactionDto, parameterizedTypeRef);
    }

    @Override
    public CreditCard transfer(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("transfer(endpoint={}, creditCardTransactionDto={})", endpoint, creditCardTransactionDto);
        return webClientPostBlock(endpoint, creditCardTransactionDto, parameterizedTypeRef);
    }

    @Override
    public CreditCard getById(Integer id) {
        LOGGER.debug("getById(endpoint={})", endpoint + "/" + id);
        return webClientGetBlock(endpoint + "/" + id, parameterizedTypeRef);
    }

}
