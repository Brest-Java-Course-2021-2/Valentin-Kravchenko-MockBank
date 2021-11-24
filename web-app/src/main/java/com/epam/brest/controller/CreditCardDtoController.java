package com.epam.brest.controller;

import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.epam.brest.constant.ControllerConstant.CARDS;

@Controller
@RequestMapping("/cards")
public class CreditCardDtoController {

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoController(CreditCardDtoService creditCardDtoService) {
        this.creditCardDtoService = creditCardDtoService;
    }

    @GetMapping()
    public String cards(Model model) {
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        model.addAttribute(CARDS, cards);
        return CARDS;
    }

}
