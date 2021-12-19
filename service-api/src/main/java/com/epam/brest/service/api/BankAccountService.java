package com.epam.brest.service.api;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.exception.BankAccountException;

import java.util.List;

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
     * @throws BankAccountException if the bank account is not found by its id
     */
    BankAccount update(BankAccount bankAccount) throws BankAccountException;

    /**
     * Removes a given bank account.
     * @param id - id of the bank account to delete
     * @return the instance of the deleted bank account
     * @throws BankAccountException if bank account has linked credit card
     * or the bank account is not found by its id
     */
    BankAccount delete(Integer id) throws BankAccountException;

    /**
     * Returns all the credit cards linked with a bank account by its id.
     * @param id - bank account id
     * @return list of credit cards
     */
    List<CreditCard> getAllCardsById(Integer id);

}
