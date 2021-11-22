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

@Controller
@RequestMapping("/card")
public class CreditCardController {

    private final CreditCardService creditCardService;
    private final Validator validator;

    @Value("${card.message.create}")
    private String createMessage;

    @Value("${card.message.remove}")
    private String removeMessage;

    @Value("${card.message.deposit}")
    private String depositMessage;

    @Value("${card.message.transfer}")
    private String transferMessage;

    public CreditCardController(CreditCardService creditCardService,
                                @Qualifier("creditCardTransactionDtoValidator") Validator validator) {
        this.creditCardService = creditCardService;
        this.validator = validator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping()
    public String create(Integer accountId, String accountNumber, RedirectAttributes redirectAttributes){
        CreditCard newCreditCard = creditCardService.create(accountId);
        redirectAttributes.addFlashAttribute("message",
                                             String.format(createMessage, newCreditCard.getNumber(), accountNumber));
        return "redirect:/accounts";
    }

    @GetMapping("{id}/deposit")
    public String deposit(@PathVariable Integer id, Model model){
        CreditCard card = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(card.getNumber());
        model.addAttribute("card", creditCardTransactionDto);
        return "transaction";
    }

    @PostMapping("{id}/deposit")
    public String deposit(@Valid @ModelAttribute("card") CreditCardTransactionDto creditCardTransactionDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "transaction";
        }
        creditCardService.deposit(creditCardTransactionDto);
        redirectAttributes.addFlashAttribute("message", String.format(depositMessage, creditCardTransactionDto.getTargetCardNumber()));
        return "redirect:/cards";
    }

    @GetMapping("{id}/transfer")
    public String transfer(@PathVariable Integer id, Model model){
        CreditCard card = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(card.getNumber());
        model.addAttribute("card", creditCardTransactionDto);
        return "transaction";
    }

    @PostMapping("{id}/transfer")
    public String transfer(@Valid @ModelAttribute("card") CreditCardTransactionDto creditCardTransactionDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "transaction";
        }
        creditCardService.transfer(creditCardTransactionDto);
        redirectAttributes.addFlashAttribute("message",
                                              String.format(transferMessage, creditCardTransactionDto.getSourceCardNumber(),
                                                            creditCardTransactionDto.getTargetCardNumber()));
        return "redirect:/cards";
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id,
                         CreditCard creditCard,
                         RedirectAttributes redirectAttributes){
        creditCard.setId(id);
        creditCardService.delete(creditCard);
        redirectAttributes.addFlashAttribute("message", String.format(removeMessage, creditCard.getNumber()));
        return "redirect:/cards";
    }

}
