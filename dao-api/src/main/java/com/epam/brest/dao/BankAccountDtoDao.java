package com.epam.brest.dao;

import com.epam.brest.model.dto.BankAccountDto;

import java.util.List;

public interface BankAccountDtoDao {
    /**
     * Retrieves all the bank accounts containing the total number of credit cards
     *
     * @return bank accounts list
     */
    List<BankAccountDto> getAllContainingTotalCards();

}
