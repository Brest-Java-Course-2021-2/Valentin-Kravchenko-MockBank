package com.epam.brest.dao;

import com.epam.brest.model.entity.BankAccount;

public interface BankAccountDao extends GenericDao<BankAccount> {
    /**
     * Return whether the bank account with specified number exists in db
     * @param accountNumber - bank account number
     * @return true - if bank account with specified account number already exists, false - otherwise
     */
    boolean isAccountNumberExists(String accountNumber);

}
