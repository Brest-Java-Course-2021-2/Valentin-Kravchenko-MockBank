package com.epam.brest.service;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;

import java.util.List;

public interface BankAccountDtoService {

    /**
     * Returns all the bank accounts, each with the number of credit cards linked with it.
     * @return the list of bank accounts
     */
    List<BankAccountDto> getAllWithTotalCards();

    /**
     * Returns all the bank accounts, each with the number of credit cards linked with it.
     * Return is carried out according to a given filter.
     * @param bankAccountFilterDto - bank account filter instance
     * @return the list of bank accounts
     */
    List<BankAccountDto> getAllWithTotalCards(BankAccountFilterDto bankAccountFilterDto);

}
