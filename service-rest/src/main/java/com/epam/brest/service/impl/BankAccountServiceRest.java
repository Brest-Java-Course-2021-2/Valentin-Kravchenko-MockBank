package com.epam.brest.service.impl;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class BankAccountServiceRest extends ServiceRestBasic implements BankAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountServiceRest.class);

    private final String endpoint;
    private final ParameterizedTypeReference<BankAccount> parameterizedTypeRef;

    public BankAccountServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        LOGGER.debug("create(endpoint={})", endpoint);
        return webClientPostBlock(endpoint, bankAccount, parameterizedTypeRef);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        LOGGER.debug("update(endpoint={})", endpoint);
        return webClientPutBlock(endpoint, bankAccount, parameterizedTypeRef);
    }

    @Override
    public BankAccount delete(Integer id) throws BankAccountException {
        LOGGER.debug("getById(endpoint={})", endpoint + "/" + id);
        return webClientDeleteBlock(endpoint + "/" + id, parameterizedTypeRef);
    }

    @Override
    public BankAccount getById(Integer id) {
        LOGGER.debug("getById(endpoint={})", endpoint + "/" + id);
        return webClientGetBlock(endpoint + "/" + id, parameterizedTypeRef);
    }

}
