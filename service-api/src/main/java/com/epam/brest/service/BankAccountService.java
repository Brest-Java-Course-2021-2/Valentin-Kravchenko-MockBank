package com.epam.brest.service;

import com.epam.brest.model.entity.BankAccount;

public interface BankAccountService {

    /**
     * Creates new bank account.
     * @param customer - full name of the bank customer holding the bank account
     * @return the created bank account
     */
    BankAccount create(String customer);

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
     */
    Integer delete(BankAccount bankAccount);

}
