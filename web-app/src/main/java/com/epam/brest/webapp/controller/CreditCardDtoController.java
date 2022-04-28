package com.epam.brest.webapp.controller;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.CreditCardDto;
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
        LOGGER.debug("cards(/cards, method=GET)");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        model.addAttribute(CARDS, cards);
        LOGGER.debug("cards(/cards, method=GET, model={})", model);
        return CARDS;
    }

    @PostMapping
    public String cards(@Valid @ModelAttribute(FILTER) CreditCardFilterDto creditCardFilterDto,
                        BindingResult bindingResult,
                        Model model) {
        LOGGER.debug("cards(/cards, method=POST, creditCardDateRangeDto={})", creditCardFilterDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("cards(/cards, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return CARDS;
        }
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        LOGGER.debug("cards(/cards, method=POST, cards={})", cards);
        if (cards.isEmpty()) {
            model.addAttribute(ERROR, filterError);
        }
        model.addAttribute(CARDS, cards);
        LOGGER.debug("cardsPOST(/cards, method=POST, model={})", model);
        return CARDS;
    }

}
