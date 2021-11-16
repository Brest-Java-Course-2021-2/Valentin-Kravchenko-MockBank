package com.epam.brest.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Value("${account.create}")
    private String accountCreateMessage;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping()
    public String get() {
        return "account";
    }

    @GetMapping("{id}")
    public String get(@PathVariable Integer id, Model model) {
        BankAccount account = bankAccountService.getById(id);
        model.addAttribute("account", account);
        return "account";
    }

    @PostMapping()
    public String create(BankAccount bankAccount, RedirectAttributes redirectAttributes){
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        redirectAttributes.addFlashAttribute("message", String.format(accountCreateMessage, createdBankAccount.getNumber()));
        return "redirect:/accounts";
    }

    @PostMapping("{id}")
    public String update(@PathVariable Integer id, BankAccount bankAccount){
        bankAccount.setId(id);
        bankAccountService.update(bankAccount);
        return "redirect:/accounts";
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id, BankAccount bankAccount){
        bankAccount.setId(id);
        bankAccountService.delete(bankAccount);
        return "redirect:/accounts";
    }

}
