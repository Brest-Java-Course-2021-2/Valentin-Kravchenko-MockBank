package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountDtoController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoController.class);

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoController(BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @GetMapping()
    public ResponseEntity<List<BankAccountDto>> accounts() {
        LOGGER.debug("accountsGET(/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping()
    public ResponseEntity<List<BankAccountDto>> accounts(@Valid @RequestBody BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("accountsPOST(/accounts, bankAccountFilterDto={})", bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        return ResponseEntity.ok(accounts);
    }

}
