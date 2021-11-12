package com.epam.brest.service;

import com.epam.brest.model.dto.BankAccountDto;

import java.util.List;

public interface BankAccountDtoService {

    /**
     * Retrieves all bank accounts, each with the number of credit cards linked with it.
     * @return list of bank accounts
     */
    List<BankAccountDto> getAllWithTotalCards();

}
