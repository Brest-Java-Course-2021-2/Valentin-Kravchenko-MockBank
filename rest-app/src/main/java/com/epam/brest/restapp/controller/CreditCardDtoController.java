package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CreditCardDtoController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoController.class);

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoController(CreditCardDtoService creditCardDtoServiceImpl) {
        this.creditCardDtoService = creditCardDtoServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<CreditCardDto>> cards() {
        LOGGER.debug("cardsGET(/cards)");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<List<CreditCardDto>> cards(@Valid @RequestBody CreditCardDateRangeDto creditCardDateRangeDto) {
        LOGGER.debug("cardsPOST(/cards, creditCardDateRangeDto={})", creditCardDateRangeDto);
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        return ResponseEntity.ok(cards);
    }

}
