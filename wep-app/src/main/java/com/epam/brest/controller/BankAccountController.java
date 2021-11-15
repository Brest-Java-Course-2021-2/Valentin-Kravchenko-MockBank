package com.epam.brest.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping()
    public final String getAccount() {
        return "account";
    }

    @GetMapping( "{id}")
    public final String getAccount(@PathVariable Integer id, Model model) {
        BankAccount account = bankAccountService.getById(id);
        model.addAttribute("account", account);
        return "account";
    }

    @PostMapping()
    public final String postAccount(BankAccount bankAccount){
        BankAccount account = bankAccountService.create(bankAccount);
        return "redirect:/accounts";
    }

    @PostMapping( "{id}")
    public final String postAccount(@PathVariable Integer id, BankAccount bankAccount){
        bankAccount.setId(id);
        bankAccountService.update(bankAccount);
        return "redirect:/accounts";
    }

}
