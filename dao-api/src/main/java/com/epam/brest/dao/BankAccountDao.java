package com.epam.brest.dao;

import com.epam.brest.model.entity.BankAccount;

public interface BankAccountDao extends GenericDao<BankAccount> {

    boolean isAccountNumberExists(String accountNumber);

}
