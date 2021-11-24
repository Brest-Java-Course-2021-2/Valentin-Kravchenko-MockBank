package com.epam.brest.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.brest.constant.ControllerConstant.*;

@Controller
@RequestMapping("/card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Value("${card.message.create}")
    private String createMessage;

    @Value("${card.message.remove}")
    private String removeMessage;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping()
    public String create(Integer accountId, String accountNumber, RedirectAttributes redirectAttributes){
        CreditCard newCreditCard = creditCardService.create(accountId);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(createMessage, newCreditCard.getNumber(), accountNumber));
        return REDIRECT_ACCOUNTS;
    }

    @PostMapping("{id}/remove")
    public String remove(@PathVariable Integer id,
                         CreditCard creditCard,
                         RedirectAttributes redirectAttributes){
        creditCard.setId(id);
        creditCardService.delete(creditCard);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(removeMessage, creditCard.getNumber()));
        return REDIRECT_CARDS;
    }

}
