package com.epam.brest.webapp.controller;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
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

import javax.validation.Valid;
import java.util.List;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Controller
@RequestMapping("/cards")
public class CreditCardDtoController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoController.class);

    private final CreditCardDtoService creditCardDtoService;
    private final Validator validator;

    @Value("${card.filter.error}")
    private String filterError;

    public CreditCardDtoController(CreditCardDtoService creditCardDtoServiceRest,
                                   Validator creditCardDateRangeDtoValidator) {
        this.creditCardDtoService = creditCardDtoServiceRest;
        this.validator = creditCardDateRangeDtoValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String cards(Model model) {
        LOGGER.debug("cardsGET(/cards)");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        model.addAttribute(CARDS, cards);
        LOGGER.debug("cards(model={})", model);
        return CARDS;
    }

    @PostMapping
    public String cards(@Valid @ModelAttribute(FILTER) CreditCardDateRangeDto creditCardDateRangeDto,
                        BindingResult bindingResult,
                        Model model) {
        LOGGER.debug("cardsPOST(/cards, creditCardDateRangeDto={})", creditCardDateRangeDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("cardsPOST(/cards, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return CARDS;
        }
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        if (cards.isEmpty()) {
            LOGGER.warn("cardsPOST(/accounts, accounts={})", cards);
            model.addAttribute(ERROR, filterError);
        }
        model.addAttribute(CARDS, cards);
        LOGGER.debug("cardsPOST(model={})", model);
        return CARDS;
    }

}
