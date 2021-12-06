package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Controller
@RequestMapping("/card")
public class CreditCardController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardController.class);

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
        LOGGER.debug("get(/card, accountId={}, accountNumber={})", accountId, accountNumber);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        LOGGER.debug("create(/card, createdCreditCard={})", createdCreditCard);
        redirectAttributes.addFlashAttribute(MESSAGE, String.format(createMessage, createdCreditCard.getNumber(), accountNumber));
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

}
