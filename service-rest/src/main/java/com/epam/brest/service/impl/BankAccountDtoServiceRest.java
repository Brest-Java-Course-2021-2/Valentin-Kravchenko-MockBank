package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.BankAccountDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class BankAccountDtoServiceRest extends ServiceRestBasic implements BankAccountDtoService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoServiceRest.class);

    private final String endpoint;
    private final ParameterizedTypeReference<List<BankAccountDto>> parameterizedTypeRef;

    public BankAccountDtoServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards() {
        LOGGER.debug("getAllWithTotalCards(endpoint={})", endpoint);
        return getBlock(endpoint, parameterizedTypeRef);
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards(BankAccountsFilterDto bankAccountsFilterDto) {
        LOGGER.debug("getAllWithTotalCards(endpoint={}, bankAccountFilterDto={})", endpoint, bankAccountsFilterDto);
        return postBlock(endpoint, bankAccountsFilterDto, parameterizedTypeRef);
    }

}
