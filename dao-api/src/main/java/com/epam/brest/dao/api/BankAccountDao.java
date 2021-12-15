package com.epam.brest.dao.api;

import com.epam.brest.model.entity.BankAccount;

import java.util.List;

public interface BankAccountDao extends GenericDao<BankAccount> {

    /**
     * Returns whether the bank account with a given number exists in db.
     * @param number - bank account number
     * @return true - if bank account with specified number already exists, false - otherwise
     */
    boolean isAccountNumberExists(String number);

    /**
     * Returns all the credit card numbers linked with a bank account by its id.
     * @param id - bank account id
     * @return the list of all credit card numbers
     */
    List<String> getLinkedCards(Integer id);

}
