package com.epam.brest.restopenapi.impl;

import com.epam.brest.service.api.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BankAccountApiDelegateImpl {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountApiDelegateImpl.class);

    private final BankAccountService bankAccountService;

    public BankAccountApiDelegateImpl(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /*@Override
    public ResponseEntity<BankAccount> getBankAccountById(Integer id) {
        LOGGER.debug("getBankAccountById(id={})", id);
        BankAccount bankAccount = bankAccountService.getById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @Override
    public ResponseEntity<BankAccount> createBankAccount(CreatedBankAccount createdBankAccount) {
        LOGGER.debug("createBankAccount(createdBankAccount={})", createdBankAccount);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(createdBankAccount.getCustomer());
        BankAccount persistBankAccount = bankAccountService.create(bankAccount);
        LOGGER.debug("createBankAccount(persistBankAccount={})", persistBankAccount);
        return ResponseEntity.ok(persistBankAccount);
    }*/

}
