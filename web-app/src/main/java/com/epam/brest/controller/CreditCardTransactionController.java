package com.epam.brest.controller;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
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

import static com.epam.brest.constant.ControllerConstant.*;

@Controller
@RequestMapping("/transaction/card")
public class CreditCardTransactionController {

    private final CreditCardService creditCardService;
    private final Validator validator;

    @Value("${card.message.deposit}")
    private String depositMessage;

    @Value("${card.message.transfer}")
    private String transferMessage;

    public CreditCardTransactionController(CreditCardService creditCardService,
                                           @Qualifier("creditCardTransactionDtoValidator") Validator validator) {
        this.creditCardService = creditCardService;
        this.validator = validator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping("{id}/deposit")
    public String deposit(@PathVariable Integer id, Model model) {
        CreditCard card = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(card.getNumber());
        model.addAttribute(CARD, creditCardTransactionDto);
        return TRANSACTION;
    }

    @PostMapping("{id}/deposit")
    public String deposit(@Valid @ModelAttribute(CARD) CreditCardTransactionDto creditCardTransactionDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return TRANSACTION;
        }
        creditCardService.deposit(creditCardTransactionDto);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(depositMessage, creditCardTransactionDto.getTargetCardNumber()));
        return REDIRECT_CARDS;
    }

    @GetMapping("{id}/transfer")
    public String transfer(@PathVariable Integer id, Model model) {
        CreditCard card = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(card.getNumber());
        model.addAttribute(CARD, creditCardTransactionDto);
        return TRANSACTION;
    }

    @PostMapping("{id}/transfer")
    public String transfer(@Valid @ModelAttribute(CARD) CreditCardTransactionDto creditCardTransactionDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return TRANSACTION;
        }
        creditCardService.transfer(creditCardTransactionDto);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(transferMessage, creditCardTransactionDto.getSourceCardNumber(),
                                             creditCardTransactionDto.getTargetCardNumber()));
        return REDIRECT_CARDS;
    }

}
