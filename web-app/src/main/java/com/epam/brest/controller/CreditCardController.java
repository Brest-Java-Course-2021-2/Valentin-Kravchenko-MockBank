package com.epam.brest.controller;

import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardTransferDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Value("${card.message.create}")
    private String createMessage;

    @Value("${card.message.remove}")
    private String removeMessage;

    @Value("${card.message.deposit}")
    private String depositMessage;

    @Value("${card.message.transfer}")
    private String transferMessage;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
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
        CreditCardDepositDto creditCardDepositDto = new CreditCardDepositDto();
        creditCardDepositDto.setTargetCardNumber(card.getNumber());
        model.addAttribute("card", creditCardDepositDto);
        return "transaction";
    }

    @PostMapping("{id}/deposit")
    public String deposit(CreditCardDepositDto creditCardDepositDto,
                          RedirectAttributes redirectAttributes){
        creditCardService.deposit(creditCardDepositDto);
        redirectAttributes.addFlashAttribute("message", String.format(depositMessage,
                                                                                  creditCardDepositDto.getTargetCardNumber()));
        return "redirect:/cards";
    }

    @GetMapping("{id}/transfer")
    public String transfer(@PathVariable Integer id, Model model){
        CreditCard card = creditCardService.getById(id);
        CreditCardTransferDto creditCardTransferDto = new CreditCardTransferDto();
        creditCardTransferDto.setSourceCardNumber(card.getNumber());
        model.addAttribute("card", creditCardTransferDto);
        return "transaction";
    }

    @PostMapping("{id}/transfer")
    public String transfer(CreditCardTransferDto creditCardTransferDto,
                           RedirectAttributes redirectAttributes){
        creditCardService.transfer(creditCardTransferDto);
        redirectAttributes.addFlashAttribute("message", String.format(transferMessage,
                                                                                  creditCardTransferDto.getSourceCardNumber(),
                                                                                  creditCardTransferDto.getTargetCardNumber()));
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
