package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardService;
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

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Controller
@RequestMapping("/card")
public class CreditCardController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardController.class);

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;
    private final Validator validator;

    @Value("${card.message.deposit}")
    private String depositMessage;

    @Value("${card.message.transfer}")
    private String transferMessage;

    @Value("${card.message.create}")
    private String createMessage;

    @Value("${card.message.remove}")
    private String removeMessage;

    public CreditCardController(CreditCardService creditCardService,
                                BankAccountService bankAccountService,
                                Validator creditCardTransactionDtoValidator) {
        this.creditCardService = creditCardService;
        this.bankAccountService = bankAccountService;
        this.validator = creditCardTransactionDtoValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping("{id}/deposit")
    public String deposit(@PathVariable Integer id, Model model) {
        LOGGER.debug("depositGET(/transaction/card/{}/deposit)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        model.addAttribute(CARD, creditCardTransactionDto);
        LOGGER.debug("depositGET(/transaction/card/{}/deposit, model={})", id, model);
        return TRANSACTION;
    }

    @GetMapping("{id}/transfer")
    public String transfer(@PathVariable Integer id, Model model) {
        LOGGER.debug("transferGET(/transaction/card/{}/transfer)", id);
        CreditCard card = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(card.getNumber());
        model.addAttribute(CARD, creditCardTransactionDto);
        LOGGER.debug("transferGET(/transaction/card/{}/transfer, model={})", id, model);
        return TRANSACTION;
    }

    @PostMapping()
    public String create(Integer accountId, RedirectAttributes redirectAttributes){
        LOGGER.debug("create(/card, accountId={})", accountId);
        BankAccount bankAccountFromDb = bankAccountService.getById(accountId);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        LOGGER.debug("create(/card, createdCreditCard={})", createdCreditCard);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(createMessage, createdCreditCard.getNumber(),
                                                                    bankAccountFromDb.getNumber()));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        LOGGER.debug("remove(/card/{}/remove)", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        LOGGER.debug("remove(/account/{}/remove, deletedCreditCard={})", id, deletedCreditCard);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(removeMessage, deletedCreditCard.getNumber()));
        return REDIRECT_CARDS;
    }

    @PostMapping("{id}/deposit")
    public String deposit(@PathVariable Integer id,
                          @Valid @ModelAttribute(CARD) CreditCardTransactionDto creditCardTransactionDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        LOGGER.debug("depositPOST(/transaction/card/{}/deposit, card={})", id, creditCardTransactionDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("depositPOST(/transaction/card/{}/deposit, errorFields={})",
                    id, ControllerUtils.extractErrorFields(bindingResult));
            return TRANSACTION;
        }
        boolean isDeposit = creditCardService.deposit(creditCardTransactionDto);
        LOGGER.debug("depositPOST(/transaction/card/{}/deposit, result={})", id, isDeposit);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(depositMessage, creditCardTransactionDto.getTargetCardNumber()));
        return REDIRECT_CARDS;
    }

    @PostMapping("{id}/transfer")
    public String transfer(@PathVariable Integer id,
                           @Valid @ModelAttribute(CARD) CreditCardTransactionDto creditCardTransactionDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        LOGGER.debug("transferPOST(/transaction/card/{}/transfer, card={})", id, creditCardTransactionDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("transferPOST(/transaction/card/{}/transfer, errorFields={})",
                    id, ControllerUtils.extractErrorFields(bindingResult));
            return TRANSACTION;
        }
        boolean isTransfer = creditCardService.transfer(creditCardTransactionDto);
        LOGGER.debug("transferPOST(/transaction/card/{}/transfer, result={})", id, isTransfer);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(transferMessage, creditCardTransactionDto.getSourceCardNumber(),
                creditCardTransactionDto.getTargetCardNumber()));
        return REDIRECT_CARDS;
    }

}
