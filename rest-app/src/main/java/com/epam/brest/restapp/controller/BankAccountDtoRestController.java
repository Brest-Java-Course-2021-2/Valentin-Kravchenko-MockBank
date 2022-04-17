package com.epam.brest.restapp.controller;

import com.epam.brest.faker.api.FakerService;
import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
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
import java.util.Locale;
import java.util.Optional;

@Tag(name = "Bank Account", description = "The Bank Account API")
@RestController
@RequestMapping("api/accounts")
public class BankAccountDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoRestController.class);

    private final BankAccountDtoService bankAccountDtoService;
    private final FakerService<BankAccountDto> fakerService;

    public BankAccountDtoRestController(BankAccountDtoService bankAccountDtoServiceImpl,
                                        FakerService<BankAccountDto> BankAccountDtoFakerServiceImpl) {
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
        this.fakerService = BankAccountDtoFakerServiceImpl;
    }

    @Operation(summary = "List of all bank accounts",
               operationId = "getAccounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200")}
    )
    @GetMapping
    public ResponseEntity<List<BankAccountDto>> getAccounts() {
        LOGGER.debug("accountsGET(api/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        LOGGER.debug("accounts={}", accounts);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "List of the fake bank accounts",
               operationId = "getFakeAccounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200")}
    )
    @GetMapping("/fake")
    public ResponseEntity<List<BankAccountDto>> getFakeAccounts(
            @Parameter(description = "Volume of fake data", example = "10")
            @RequestParam Optional<Integer> dataVolume,
            @Parameter(description = "Locale for generating fake data",
                       schema = @Schema(implementation = String.class),
                       example = "en")
            @RequestParam Optional<Locale> locale
    ) {
        LOGGER.debug("getFakeAccounts(api/accounts/fake)");
        List<BankAccountDto> accounts = fakerService.getFakeData(dataVolume, locale);
        LOGGER.debug("accounts={}", accounts);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "List of filtered bank accounts",
               operationId = "getFilteredAccounts",
               description = "Filter performs by account number and customer full name. " +
                             "The bank account number and/or bank customer full name search pattern should be specified. " +
                             "Valid format of the account number search pattern is FirstNumberPattern(required, up to 17 characters){space}SecondNumberPattern(optional, up to 17 characters). " +
                             "For example, BY, BY 99T6. " +
                             "Allowed characters for the account number search pattern are [A-Z0-9]. " +
                             "Valid format of the customer search pattern is FirstNamePattern(up to 63 characters){space}LastNamePattern(up to 64 characters). " +
                             "For example, Iv Iva, an ov, ov, Iv. " +
                             "Allowed characters for the customer search pattern are [A-Za-z]",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrorsMessage")),
                                         responseCode = "400", description = "If any search pattern is invalid")}
    )
    @PostMapping
    public ResponseEntity<List<BankAccountDto>> getAccounts(
            @Parameter(required = true)
            @Valid @RequestBody BankAccountFilterDto bankAccountFilterDto
    ) {
        LOGGER.debug("accountsPOST(api/accounts, bankAccountFilterDto={})", bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        LOGGER.debug("accounts={}", accounts);
        return ResponseEntity.ok(accounts);
    }

}
