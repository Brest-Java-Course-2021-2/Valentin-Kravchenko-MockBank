package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.api.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/account")
public class BankAccountController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping()
    public ResponseEntity<BankAccount> get() {
        LOGGER.debug("get(/account)");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRegistrationDate(LocalDate.now());
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("{id}")
    public ResponseEntity<BankAccount> get(@PathVariable Integer id) {
        LOGGER.debug("get(/account/{})", id);
        BankAccount bankAccount = bankAccountService.getById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping()
    public ResponseEntity<BankAccount> create(@Valid @RequestBody BankAccount bankAccount){
        LOGGER.debug("create(/account, bankAccount={})", bankAccount);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        LOGGER.debug("create(/account, createdBankAccount={})", createdBankAccount);
        return ResponseEntity.ok(createdBankAccount);
    }

    @PutMapping()
    public ResponseEntity<BankAccount> update(@Valid @RequestBody BankAccount bankAccount) {
        LOGGER.debug("update(/account, bankAccount={})", bankAccount);
        BankAccount updatedBankAccount = bankAccountService.update(bankAccount);
        LOGGER.debug("update(/account, updatedBankAccount={})", updatedBankAccount);
        return ResponseEntity.ok(updatedBankAccount);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BankAccount> delete(@PathVariable Integer id) {
        LOGGER.debug("delete(/account, id={})", id);
        BankAccount deletedBankAccount = bankAccountService.delete(id);
        LOGGER.debug("delete(/account, updatedBankAccount={})", deletedBankAccount);
        return ResponseEntity.ok(deletedBankAccount);
    }

}
