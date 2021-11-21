package com.epam.brest.dao;

import com.epam.brest.model.entity.BankAccount;

import java.util.List;

public interface BankAccountDao extends GenericDao<BankAccount> {

    /**
     * Return whether the bank account with specified number exists in db.
     * @param number - bank account number
     * @return true - if bank account with specified number already exists, false - otherwise
     */
    boolean isAccountNumberExists(String number);

    /**
     * Return all credit card numbers linked with a bank account.
     * @param bankAccount - bank account instance
     * @return list of all credit card numbers
     */
    List<String> getLinkedCards(BankAccount bankAccount);

}
