package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/account")
public class BankAccountRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountRestController.class);

    private final BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountServiceImpl) {
        this.bankAccountService = bankAccountServiceImpl;
    }

    @GetMapping
    public ResponseEntity<BankAccount> get() {
        LOGGER.debug("get(api/account)");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRegistrationDate(LocalDate.now());
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("{id}")
    public ResponseEntity<BankAccount> get(@PathVariable Integer id) {
        LOGGER.debug("get(api/account/{})", id);
        BankAccount bankAccount = bankAccountService.getById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("{id}/cards")
    public ResponseEntity<List<CreditCard>> getAllCards(@PathVariable Integer id) {
        LOGGER.debug("get(api/account/{}/cards)", id);
        List<CreditCard> cards = bankAccountService.getAllCardsById(id);
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<BankAccount> create(@Valid @RequestBody BankAccount bankAccount){
        LOGGER.debug("create(api/account, bankAccount={})", bankAccount);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        LOGGER.debug("create(api/account, createdBankAccount={})", createdBankAccount);
        return ResponseEntity.ok(createdBankAccount);
    }

    @PutMapping
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
