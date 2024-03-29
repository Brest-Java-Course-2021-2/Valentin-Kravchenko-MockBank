package com.epam.brest.restapp.controller;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@Tag(name = "Credit Card", description = "The Credit Card API")
@RestController
@RequestMapping("api/cards")
public class CreditCardDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoRestController.class);

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoRestController(CreditCardDtoService creditCardDtoServiceImpl) {
        this.creditCardDtoService = creditCardDtoServiceImpl;
    }

    @Operation(summary = "List of all credit cards",
               operationId = "getCards",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(ref = "#/components/schemas/creditCardDto"))),
                                         responseCode = "200")}
    )
    @GetMapping
    public ResponseEntity<List<CreditCardDto>> getCards() {
        LOGGER.debug("getCards(api/cards, method=GET)");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        LOGGER.debug("cards={}", cards);
        return ok(cards);
    }

    @Operation(summary = "List of filtered credit cards",
               operationId = "getFilteredCards",
               description = "Filter performs by credit card expiration date. " +
                             "The start date and/or end date of the credit card expiration range should be specified. " +
                             "Valid date format is Month{required, 2 digits}\\/Year{required, 4 digits} (for example, 06/2022)",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(ref = "#/components/schemas/creditCardDto"))),
                                         responseCode = "200"),
                            @ApiResponse(responseCode = "204", description = "No search results found", content = @Content),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "If the date format is incorrect")}
    )
    @PostMapping
    public ResponseEntity<List<CreditCardDto>> getCards(
            @Parameter(required = true)
            @Valid @RequestBody CreditCardFilterDto creditCardFilterDto
    ) {
        LOGGER.debug("getCards(api/cards, method=POST, creditCardDateRangeDto={})", creditCardFilterDto);
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        LOGGER.debug("cards={}", cards);
        return cards.isEmpty() ? noContent().build() : ok(cards);
    }

}
