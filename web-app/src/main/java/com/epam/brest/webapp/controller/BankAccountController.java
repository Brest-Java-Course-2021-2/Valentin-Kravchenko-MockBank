package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.webapp.util.ControllerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Controller
@RequestMapping("/account")
public class BankAccountController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;
    private final Validator validator;

    @Value("${account.message.create}")
    private String createMessage;

    @Value("${account.message.update}")
    private String updateMessage;

    @Value("${account.message.remove}")
    private String removeMessage;

    public BankAccountController(BankAccountService bankAccountService,
                                 Validator bankAccountValidator) {
        this.bankAccountService = bankAccountService;
        this.validator = bankAccountValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String get(Model model) {
        LOGGER.debug("get(/account)");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRegistrationDate(LocalDate.now());
        model.addAttribute(ACCOUNT, bankAccount);
        LOGGER.debug("get(model={})", model);
        return ACCOUNT;
    }

    @GetMapping("{id}")
    public String get(@PathVariable Integer id, Model model) {
        LOGGER.debug("get(/account/{})", id);
        BankAccount bankAccount = bankAccountService.getById(id);
        model.addAttribute(ACCOUNT, bankAccount);
        LOGGER.debug("get(model={})", model);
        return ACCOUNT;
    }

    @PostMapping
    public String create(@Valid @ModelAttribute(ACCOUNT) BankAccount bankAccount,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        LOGGER.debug("create(/account, bankAccount={})", bankAccount);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("create(/account, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return ACCOUNT;
        }
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        LOGGER.debug("create(/account, createdBankAccount={})", createdBankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(createMessage, createdBankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute(ACCOUNT) BankAccount bankAccount,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        LOGGER.debug("update(/account/{}, bankAccount={})", id, bankAccount);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("update(/account, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return ACCOUNT;
        }
        bankAccount.setId(id);
        BankAccount updatedBankAccount = bankAccountService.update(bankAccount);
        LOGGER.debug("update(/account/{}, updatedBankAccount={})", id, updatedBankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(updateMessage, updatedBankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        LOGGER.debug("remove(/account/{}/remove)", id);
        BankAccount deletedBankAccount = bankAccountService.delete(id);
        LOGGER.debug("remove(/account/{}/remove, deletedBankAccount={})", id, deletedBankAccount);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(removeMessage, deletedBankAccount.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

}
