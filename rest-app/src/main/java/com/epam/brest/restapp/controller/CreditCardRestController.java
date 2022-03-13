package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Credit Cards API")
@RestController
@RequestMapping("api/card")
public class CreditCardRestController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardRestController.class);

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;

    public CreditCardRestController(CreditCardService creditCardServiceImpl,
                                    BankAccountService bankAccountServiceImpl) {
        this.creditCardService = creditCardServiceImpl;
        this.bankAccountService = bankAccountServiceImpl;
    }

    @Operation(summary = "Get a credit card by its ID",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the credit card with the given ID not found")})
    @GetMapping("{id}")
    public ResponseEntity<CreditCard> getById(
            @Parameter(description = "Credit card ID", required = true)
            @PathVariable Integer id
    ) {
        LOGGER.debug("getById(api/card/{})", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        return ResponseEntity.ok(creditCardFromDb);
    }

    @Operation(hidden = true)
    @GetMapping("{id}/deposit")
    public ResponseEntity<CreditCardTransactionDto> deposit(@PathVariable Integer id) {
        LOGGER.debug("depositGET(api/card/{}/deposit)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @Operation(hidden = true)
    @GetMapping("{id}/transfer")
    public ResponseEntity<CreditCardTransactionDto> transfer(@PathVariable Integer id) {
        LOGGER.debug("transferGET(api/card/{}/transfer)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @Operation(summary = "Create a new credit card",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the bank account with the given ID not found")})
    @PostMapping
    public ResponseEntity<CreditCard> create(
            @Parameter(description = "Bank account ID",
                       schema = @Schema(ref = "#/components/schemas/createCreditCard"),
                       required = true)
            @RequestBody Integer accountId
    ){
        LOGGER.debug("create(api/card, accountId={})", accountId);
        bankAccountService.getById(accountId);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        return ResponseEntity.ok(createdCreditCard);
    }

    @Operation(summary = "Deposit money to a credit card",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the credit card with the given number not found"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrors")),
                                         responseCode = "400", description = "if the target credit card number is invalid")})
    @PostMapping("deposit")
    public ResponseEntity<CreditCard> deposit(
            @Parameter(description = "Credit card transaction DTO",
                       schema = @Schema(ref = "#/components/schemas/depositMoney"),
                       required = true)
            @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto
    ) {
        LOGGER.debug("depositPOST(api/card/deposit, card={})", creditCardTransactionDto);
        CreditCard targetCreditCard = creditCardService.deposit(creditCardTransactionDto);
        return ResponseEntity.ok(targetCreditCard);
    }

    @Operation(summary = "Transfer money from a credit card",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the credit cards were not found by their numbers"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrors")),
                                         responseCode = "400", description = "if the source credit card doesn't contain enough money for a transfer")})
    @PostMapping("transfer")
    public ResponseEntity<CreditCard> transfer(
            @Parameter(description = "Credit card transaction DTO",
                       schema = @Schema(ref = "#/components/schemas/transferMoney"),
                       required = true)
            @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto
    ) {
        LOGGER.debug("transferPOST(api/card/transfer, card={})", creditCardTransactionDto);
        CreditCard sourceCreditCard = creditCardService.transfer(creditCardTransactionDto);
        return ResponseEntity.ok(sourceCreditCard);
    }

    @Operation(summary = "Delete credit card by its ID",
            responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                      responseCode = "200"),
                         @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                      responseCode = "404", description = "If the credit card with the given ID not found"),
                         @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                      responseCode = "400", description = "if the credit card has a positive balance")})
    @DeleteMapping("{id}")
    public ResponseEntity<CreditCard> remove(
            @Parameter(description = "Credit card ID", required = true)
            @PathVariable Integer id
    ){
        LOGGER.debug("remove(api/card/{})", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return ResponseEntity.ok(deletedCreditCard);
    }

}
