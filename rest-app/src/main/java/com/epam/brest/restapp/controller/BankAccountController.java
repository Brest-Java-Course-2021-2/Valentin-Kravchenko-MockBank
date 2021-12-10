package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
