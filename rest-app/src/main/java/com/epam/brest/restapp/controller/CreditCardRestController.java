package com.epam.brest.restapp.controller;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.CreditCard;
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

import static org.springframework.http.ResponseEntity.ok;

@Tag(name = "Credit Card", description = "The Credit Card API")
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
               operationId = "getCreditCardById",
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
        return ok(creditCardFromDb);
    }

    @Operation(summary = "Create a new credit card",
            operationId = "createCreditCard",
            responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                      responseCode = "200"),
                         @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                      responseCode = "404", description = "If the bank account with the given ID not found")})
    @PostMapping
    public ResponseEntity<CreditCard> create(
            @Parameter(description = "Bank account ID to which the credit card will be linked",
                       schema = @Schema(ref = "#/components/schemas/bankAccountId"),
                       required = true)
            @RequestBody Integer accountId
    ){
        LOGGER.debug("create(api/card, accountId={})", accountId);
        bankAccountService.getById(accountId);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        return ok(createdCreditCard);
    }

    @Operation(summary = "Delete a credit card by its ID",
            operationId = "deleteCreditCard",
            responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                      responseCode = "200"),
                         @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                      responseCode = "404", description = "If the credit card with the given ID not found"),
                         @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                      responseCode = "400", description = "if the credit card has a positive balance")})
    @DeleteMapping("{id}")
    public ResponseEntity<CreditCard> delete(
            @Parameter(description = "Credit card ID", required = true)
            @PathVariable Integer id
    ){
        LOGGER.debug("delete(api/card/{})", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return ok(deletedCreditCard);
    }

    @Operation(summary = "Execute a deposit transaction for a specific credit card",
               operationId = "depositMoney",
               description = "Deposits a specified sum of money to the balance of the target credit card. " +
                             "Valid format of the sum of money is {up to 6 digits}{, or .}{up to 2 digits}. " +
                             "For example, 1025, 1730.1, 0,45. " +
                             "Decimal separator type depends on the specified locale",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the target credit card with the given number not found"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "If the target credit card number is invalid" +
                                                                             "and/or format of the sum of money is incorrect")})
    @PostMapping("deposit")
    public ResponseEntity<CreditCard> deposit(
            @Parameter(description = "Deposit transaction data",
                       schema = @Schema(ref = "#/components/schemas/depositTransactionDto"),
                       required = true)
            @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto
    ) {
        LOGGER.debug("depositPOST(api/card/deposit, card={})", creditCardTransactionDto);
        CreditCard targetCreditCard = creditCardService.deposit(creditCardTransactionDto);
        return ok(targetCreditCard);
    }

    @Operation(summary = "Execute a transfer transaction for specific credit cards",
               operationId = "transferMoney",
               description = "Transfers a specified sum of money from the balance of the source credit card " +
                             "to the balance of the target credit card. " +
                             "Valid format of the sum of money is {up to 6 digits}{, or .}{up to 2 digits}. " +
                             "For example, 1025, 1730.1, 0,45. " +
                             "Decimal separator type depends on the specified locale",
               responses = {@ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/creditCard")),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the source or target credit card was not found by number"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "if the source credit card doesn't contain enough money for a transfer " +
                                                                             "and/or format of the sum of money is incorrect")})
    @PostMapping("transfer")
    public ResponseEntity<CreditCard> transfer(
            @Parameter(description = "Transfer transaction data",
                       schema = @Schema(ref = "#/components/schemas/transferTransactionDto"),
                       required = true)
            @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto
    ) {
        LOGGER.debug("transferPOST(api/card/transfer, card={})", creditCardTransactionDto);
        CreditCard sourceCreditCard = creditCardService.transfer(creditCardTransactionDto);
        return ok(sourceCreditCard);
    }

    @Operation(hidden = true)
    @GetMapping("{id}/deposit")
    public ResponseEntity<CreditCardTransactionDto> deposit(@PathVariable Integer id) {
        LOGGER.debug("depositGET(api/card/{}/deposit)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        return ok(creditCardTransactionDto);
    }

    @Operation(hidden = true)
    @GetMapping("{id}/transfer")
    public ResponseEntity<CreditCardTransactionDto> transfer(@PathVariable Integer id) {
        LOGGER.debug("transferGET(api/card/{}/transfer)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(creditCardFromDb.getNumber());
        return ok(creditCardTransactionDto);
    }

}
