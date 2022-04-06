package com.epam.brest.openapi.impl;

import com.epam.brest.model.*;
import com.epam.brest.openapi.ApiDelegateBasic;
import com.epam.brest.openapi.api.BankAccountApiDelegate;
import com.epam.brest.service.api.BankAccountDtoService;
import com.epam.brest.service.api.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

@Service
public class BankAccountApiDelegateImpl extends ApiDelegateBasic implements BankAccountApiDelegate {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountApiDelegateImpl.class);

    private final BankAccountService bankAccountService;

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountApiDelegateImpl(BankAccountService bankAccountServiceImpl,
                                      BankAccountDtoService bankAccountDtoServiceIml,
                                      Validator validator) {
        super(validator);
        this.bankAccountService = bankAccountServiceImpl;
        this.bankAccountDtoService = bankAccountDtoServiceIml;
    }

    @Override
    public ResponseEntity<BankAccount> getBankAccountById(Integer id) {
        LOGGER.debug("getBankAccountById(id={})", id);
        BankAccount bankAccountById = bankAccountService.getById(id);
        return ResponseEntity.ok(bankAccountById);
    }

    @Override
    public ResponseEntity<BankAccount> createBankAccount(PersonalDataDto personalDataDto) {
        LOGGER.debug("createBankAccount(personalDataDto={})", personalDataDto);
        BankAccount createdBankAccount = new BankAccount();
        createdBankAccount.setCustomer(personalDataDto.getCustomer());
        validate(createdBankAccount);
        BankAccount persistBankAccount = bankAccountService.create(createdBankAccount);
        LOGGER.debug("createBankAccount(persistBankAccount={})", persistBankAccount);
        return ResponseEntity.ok(persistBankAccount);
    }

    @Override
    public ResponseEntity<BankAccount> updateBankAccount(UpdatedPersonalDataDto updatedPersonalDataDto) {
        LOGGER.debug("updateBankAccount(updatedPersonalDataDto={})", updatedPersonalDataDto);
        BankAccount updatedBankAccount = new BankAccount();
        updatedBankAccount.setId(updatedPersonalDataDto.getId());
        updatedBankAccount.setCustomer(updatedPersonalDataDto.getCustomer());
        validate(updatedBankAccount);
        BankAccount persistBankAccount = bankAccountService.update(updatedBankAccount);
        LOGGER.debug("updateBankAccount(persistBankAccount={})", persistBankAccount);
        return ResponseEntity.ok(persistBankAccount);
    }

    @Override
    public ResponseEntity<BankAccount> deleteBankAccount(Integer id) {
        LOGGER.debug("deleteBankAccount(id={})", id);
        BankAccount deletedBankAccount = bankAccountService.delete(id);
        return ResponseEntity.ok(deletedBankAccount);
    }

    @Override
    public ResponseEntity<List<CreditCard>> getAllCards(Integer id) {
        LOGGER.debug("getAllCards(id={})", id);
        List<CreditCard> cards = bankAccountService.getAllCardsById(id);
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<List<BankAccountDto>> getAccounts() {
        LOGGER.debug("getAccounts()");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        return ResponseEntity.ok(accounts);
    }

    @Override
    public ResponseEntity<List<BankAccountDto>> getFilteredAccounts(BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("getFilteredAccounts(bankAccountFilterDto={})", bankAccountFilterDto);
        validate(bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        return ResponseEntity.ok(accounts);
    }

}
