package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Credit Cards API")
@RestController
@RequestMapping("api/cards")
public class CreditCardDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoRestController.class);

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoRestController(CreditCardDtoService creditCardDtoServiceImpl) {
        this.creditCardDtoService = creditCardDtoServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<CreditCardDto>> cards() {
        LOGGER.debug("cardsGET(api/cards)");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<List<CreditCardDto>> cards(@Valid @RequestBody CreditCardDateRangeDto creditCardDateRangeDto) {
        LOGGER.debug("cardsPOST(api/cards, creditCardDateRangeDto={})", creditCardDateRangeDto);
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        return ResponseEntity.ok(cards);
    }

}
