package com.epam.brest.restapp.controller;

import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.api.BankAccountService;
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
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Bank Account", description = "The Bank Account API")
@RestController
@RequestMapping("api/account")
public class BankAccountRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountRestController.class);

    private final BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountServiceImpl) {
        this.bankAccountService = bankAccountServiceImpl;
    }

    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<BankAccount> get() {
        LOGGER.debug("get(api/account)");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRegistrationDate(LocalDate.now());
        return ResponseEntity.ok(bankAccount);
    }

    @Operation(summary = "Get a bank account by its ID",
               operationId = "getBankAccountById",
               responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BankAccount.class)),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the bank account with the given ID not found") })
    @GetMapping("{id}")
    public ResponseEntity<BankAccount> getById(
            @Parameter(description = "Bank account ID", required = true)
            @PathVariable Integer id
    ) {
        LOGGER.debug("getById(api/account/{})", id);
        BankAccount bankAccount = bankAccountService.getById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @Operation(summary = "List of all credit cards linked with a specific bank account",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(ref = "#/components/schemas/creditCard"))),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                         responseCode = "404", description = "If the bank account with the given ID not found") })
    @GetMapping("{id}/cards")
    public ResponseEntity<List<CreditCard>> getAllCards(
            @Parameter(description = "Bank account ID", required = true)
            @PathVariable Integer id
    ) {
        LOGGER.debug("get(api/account/{}/cards)", id);
        List<CreditCard> cards = bankAccountService.getAllCardsById(id);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Create a new bank account",
               operationId = "createBankAccount",
               responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BankAccount.class)),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "If the customer full name is incorrect")})
    @PostMapping
    public ResponseEntity<BankAccount> create(
            @Parameter(description = "Personal data of the bank account customer",
                       schema = @Schema(ref = "#/components/schemas/personalDataDto"),
                       required = true)
            @Valid @RequestBody BankAccount bankAccount
    ){
        LOGGER.debug("create(api/account, bankAccount={})", bankAccount);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        LOGGER.debug("create(api/account, createdBankAccount={})", createdBankAccount);
        return ResponseEntity.ok(createdBankAccount);
    }

    @Operation(summary = "Update an existing bank account",
               operationId = "updateBankAccount",
               responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BankAccount.class)),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "If the customer full name is incorrect")})
    @PutMapping
    public ResponseEntity<BankAccount> update(
            @Parameter(description = "Personal data of the bank account customer for updating",
                       schema = @Schema(ref = "#/components/schemas/updatedPersonalDataDto"),
                       required = true)
            @Valid @RequestBody BankAccount bankAccount
    ) {
        LOGGER.debug("update(/account, bankAccount={})", bankAccount);
        BankAccount updatedBankAccount = bankAccountService.update(bankAccount);
        LOGGER.debug("update(/account, updatedBankAccount={})", updatedBankAccount);
        return ResponseEntity.ok(updatedBankAccount);
    }

    @Operation(summary = "Delete a bank account by its ID",
               operationId = "deleteBankAccount",
               responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BankAccount.class)),
                                        responseCode = "200"),
                           @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                        responseCode = "404", description = "If the bank account with the given ID not found"),
                           @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/errorMessage")),
                                        responseCode = "400", description = "if the bank account has linked credit cards")})
    @DeleteMapping("{id}")
    public ResponseEntity<BankAccount> delete(
            @Parameter(description = "Bank account ID", required = true)
            @PathVariable Integer id
    ) {
        LOGGER.debug("delete(/account, id={})", id);
        BankAccount deletedBankAccount = bankAccountService.delete(id);
        LOGGER.debug("delete(/account, updatedBankAccount={})", deletedBankAccount);
        return ResponseEntity.ok(deletedBankAccount);
    }

}
