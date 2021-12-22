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
@RequestMapping("api/accounts")
public class BankAccountDtoRestController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoRestController.class);

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoRestController(BankAccountDtoService bankAccountDtoServiceImpl) {
        this.bankAccountDtoService = bankAccountDtoServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDto>> accounts() {
        LOGGER.debug("accountsGET(api/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<List<BankAccountDto>> accounts(@Valid @RequestBody BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("accountsPOST(api/accounts, bankAccountFilterDto={})", bankAccountFilterDto);
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        return ResponseEntity.ok(accounts);
    }

}
