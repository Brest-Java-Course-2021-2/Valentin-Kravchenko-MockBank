package com.epam.brest.service;

import com.epam.brest.model.entity.BankAccount;

public interface BankAccountService {

    /**
     * Retrieves a bank account by its id.
     * @param id - bank account id
     * @return the bank account with the given id
     * @throws IllegalArgumentException if none found
     */
    BankAccount getById(Integer id);

    /**
     * Creates new bank account.
     * @param bankAccount - new bank account
     * @return the created bank account
     */
    BankAccount create(BankAccount bankAccount);

    /**
     * Updates a given bank account.
     * @param bankAccount - bank account containing updated data
     * @return the number of rows affected
     */
    Integer update(BankAccount bankAccount);

    /**
     * Removes a given bank account.
     * @param bankAccount - bank account being deleted
     * @return the number of rows affected
     * @throws IllegalArgumentException if bank account has linked credit card
     */
    Integer delete(BankAccount bankAccount);

}
