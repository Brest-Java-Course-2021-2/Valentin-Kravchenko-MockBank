package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.restapp.exception.ErrorResponse;
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

@Tag(name = "Bank Accounts API")
@RestController
@RequestMapping("api/accounts")
public class BankAccountDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoRestController.class);

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoRestController(BankAccountDtoService bankAccountDtoServiceImpl) {
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
    }

    @Operation(description = "List of all bank accounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200", description = "OK")}
    )
    @GetMapping
    public ResponseEntity<List<BankAccountDto>> accounts() {
        LOGGER.debug("accountsGET(api/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        return ResponseEntity.ok(accounts);
    }

    @Operation(description = "List of filtered bank accounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                            responseCode = "200", description = "OK"),
                            @ApiResponse(content = @Content(schema = @Schema(ref = "#/components/schemas/validationErrors")),
                            responseCode = "400", description = "If the search patterns are invalid")}
    )
    @PostMapping
    public ResponseEntity<List<BankAccountDto>> accounts(
            @Parameter(description = "Search patterns", required = true)
            @Valid @RequestBody BankAccountFilterDto bankAccountFilterDto
    ) {
        LOGGER.debug("accountsPOST(api/accounts, bankAccountFilterDto={})", bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        return ResponseEntity.ok(accounts);
    }

}
