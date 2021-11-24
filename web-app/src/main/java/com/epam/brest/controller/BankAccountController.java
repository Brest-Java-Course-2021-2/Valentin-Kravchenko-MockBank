package com.epam.brest.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.time.LocalDate;

import static com.epam.brest.constant.ControllerConstant.*;

@Controller
@RequestMapping("/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final Validator validator;

    @Value("${account.message.create}")
    private String createMessage;

    @Value("${account.message.update}")
    private String updateMessage;

    @Value("${account.message.remove}")
    private String removeMessage;

    public BankAccountController(BankAccountService bankAccountService,
                                 @Qualifier("bankAccountValidator") Validator validator) {
        this.bankAccountService = bankAccountService;
        this.validator = validator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping()
    public String get(Model model) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRegistrationDate(LocalDate.now());
        model.addAttribute(ACCOUNT, bankAccount);
        return ACCOUNT;
    }

    @GetMapping("{id}")
    public String get(@PathVariable Integer id, Model model) {
        BankAccount bankAccount = bankAccountService.getById(id);
        model.addAttribute(ACCOUNT, bankAccount);
        return ACCOUNT;
    }

    @PostMapping()
    public String create(@Valid @ModelAttribute(ACCOUNT) BankAccount bankAccount,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "account";
        }
        BankAccount newBankAccount = bankAccountService.create(bankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(createMessage, newBankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute(ACCOUNT) BankAccount bankAccount,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "account";
        }
        bankAccount.setId(id);
        bankAccountService.update(bankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(updateMessage, bankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id, BankAccount bankAccount, RedirectAttributes redirectAttributes){
        bankAccount.setId(id);
        bankAccountService.delete(bankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(removeMessage, bankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

}
