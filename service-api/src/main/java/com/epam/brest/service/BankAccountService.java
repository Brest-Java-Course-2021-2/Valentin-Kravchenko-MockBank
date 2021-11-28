package com.epam.brest.service;

import com.epam.brest.model.entity.BankAccount;

public interface BankAccountService extends GenericService<BankAccount> {

    /**
     * Creates a new bank account.
     * @param bankAccount - a new bank account instance
     * @return the instance of the created bank account
     */
    BankAccount create(BankAccount bankAccount);

    /**
     * Updates a given bank account.
     * @param bankAccount - a given bank account instance
     * @return the instance of the updated bank account
     */
    BankAccount update(BankAccount bankAccount);

    /**
     * Removes a given bank account.
     * @param id - id of the bank account to delete
     * @return the instance of the deleted bank account
     * @throws IllegalArgumentException if bank account has linked credit card
     */
    BankAccount delete(Integer id);

}
