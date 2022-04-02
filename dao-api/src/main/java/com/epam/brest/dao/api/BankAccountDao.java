package com.epam.brest.dao.api;

import com.epam.brest.model.BankAccount;

public interface BankAccountDao extends GenericDao<BankAccount> {

    /**
     * Returns whether the bank account with a given number exists in db.
     * @param number - bank account number
     * @return true - if bank account with specified number already exists, false - otherwise
     */
    boolean isAccountNumberExists(String number);

}
