package com.epam.brest.restapp.controller;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/card")
public class CreditCardController {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardController.class);

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;

    public CreditCardController(CreditCardService creditCardService, BankAccountService bankAccountService) {
        this.creditCardService = creditCardService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("{id}/deposit")
    public ResponseEntity<CreditCardTransactionDto> deposit(@PathVariable Integer id) {
        LOGGER.debug("depositGET(/card/{}/deposit)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @GetMapping("{id}/transfer")
    public ResponseEntity<CreditCardTransactionDto> transfer(@PathVariable Integer id) {
        LOGGER.debug("transferGET(/card/{}/transfer)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @PostMapping()
    public ResponseEntity<CreditCard> create(@RequestBody Integer accountId){
        LOGGER.debug("create(/card, accountId={})", accountId);
        bankAccountService.getById(accountId);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        return ResponseEntity.ok(createdCreditCard);
    }

    @PostMapping("{id}/deposit")
    public ResponseEntity<Boolean> deposit(@PathVariable Integer id,
                                           @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("depositPOST(/card/{}/deposit, card={})", id, creditCardTransactionDto);
        boolean isDeposit = creditCardService.deposit(creditCardTransactionDto);
        return ResponseEntity.ok(isDeposit);
    }

    @PostMapping("{id}/transfer")
    public ResponseEntity<Boolean> transfer(@PathVariable Integer id,
                                            @Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("transferPOST(/card/{}/transfer, card={})", id, creditCardTransactionDto);
        boolean isTransfer = creditCardService.transfer(creditCardTransactionDto);
        return ResponseEntity.ok(isTransfer);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CreditCard> remove(@PathVariable Integer id){
        LOGGER.debug("remove(/card/{})", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return ResponseEntity.ok(deletedCreditCard);
    }

}
