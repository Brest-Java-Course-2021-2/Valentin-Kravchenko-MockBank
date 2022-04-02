package com.epam.brest.dao.api;

import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;

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
