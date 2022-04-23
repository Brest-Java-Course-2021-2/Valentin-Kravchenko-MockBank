package com.epam.brest.restapp.controller;

import com.epam.brest.excel.api.ExcelService;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Tag(name = "Bank Account", description = "The Bank Account API")
@RestController
@RequestMapping("api/accounts")
public class BankAccountDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoRestController.class);

    public static final String ATTACHMENT_FILENAME = "attachment; filename=%s.xlsx";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    public static final int ACCOUNTS_SHEET_IDX = 0;

    private final BankAccountDtoService bankAccountDtoService;
    private final FakerService<BankAccountDto> fakerService;
    private final ExcelService<BankAccountDto> excelService;

    public BankAccountDtoRestController(BankAccountDtoService bankAccountDtoServiceImpl,
                                        FakerService<BankAccountDto> bankAccountDtoFakerServiceImpl,
                                        ExcelService<BankAccountDto> bankAccountDtoExcelServiceImpl) {
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
        this.fakerService = bankAccountDtoFakerServiceImpl;
        this.excelService = bankAccountDtoExcelServiceImpl;
    }

    @Operation(summary = "List of all bank accounts",
               operationId = "getAccounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200")}
    )
    @GetMapping
    public ResponseEntity<List<BankAccountDto>> getAccounts() {
        LOGGER.debug("getAccounts(api/accounts, method=GET)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        LOGGER.debug("accounts={}", accounts);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Export all bank accounts into Excel sheet",
               operationId = "exportAccountsIntoExcelSheet",
               description = "Creates a file in an Excel Open XML Spreadsheet (XLSX) format containing data on all bank accounts. " +
                             "The file name is Accounts.xlsx",
               responses = {@ApiResponse(content = @Content(schema = @Schema(type = "string", format = "byte")),
                                         responseCode = "200")}
    )
    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportAccountsIntoExcelSheet() {
        LOGGER.debug("exportAccountsIntoExcelSheet(api/accounts/export/excel, method=GET)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        Workbook workbook = excelService.createWorkbook(accounts);
        String fileName = workbook.getSheetName(ACCOUNTS_SHEET_IDX);
        ByteArrayResource resource = excelService.export(workbook);
        return ResponseEntity.ok()
                             .header(CONTENT_DISPOSITION, format(ATTACHMENT_FILENAME, fileName))
                             .contentType(MediaType.parseMediaType(APPLICATION_VND_MS_EXCEL))
                             .contentLength(resource.contentLength())
                             .body(resource);
    }

    @Operation(summary = "List of fake bank accounts",
               operationId = "getFakeAccounts",
               responses = {@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankAccountDto.class))),
                                         responseCode = "200")}
    )
    @GetMapping("/fake")
    public ResponseEntity<List<BankAccountDto>> getFakeAccounts(
            @Parameter(description = "Amount of fake data",
                       example = "10")
            @RequestParam Optional<Integer> amount,
            @Parameter(description = "Locale for generating fake data",
                       schema = @Schema(implementation = String.class),
                       example = "en")
            @RequestParam Optional<Locale> locale
    ) {
        LOGGER.debug("getFakeAccounts(api/accounts/fake, method=GET, amount={}, locale={})", amount, locale);
        List<BankAccountDto> accounts = fakerService.getFakeData(amount, locale);
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
        LOGGER.debug("getAccounts(api/accounts, method=POST, bankAccountFilterDto={})", bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        LOGGER.debug("accounts={}", accounts);
        return ResponseEntity.ok(accounts);
    }

}
