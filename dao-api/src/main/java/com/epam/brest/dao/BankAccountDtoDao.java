package com.epam.brest.dao;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;

import java.util.List;

public interface BankAccountDtoDao {

    /**
     * Retrieves all the bank accounts, each with the number of credit cards linked with it.
     * @return the list of bank accounts
     */
    List<BankAccountDto> getAllWithTotalCards();

    /**
     * Retrieves all the bank accounts, each with the number of credit cards linked with it.
     * Retrieving is carried out according to a given filter.
     * @param bankAccountFilterDto - bank account filter instance
     * @return the list of bank accounts
     */
    List<BankAccountDto> getAllWithTotalCards(BankAccountFilterDto bankAccountFilterDto);

}
