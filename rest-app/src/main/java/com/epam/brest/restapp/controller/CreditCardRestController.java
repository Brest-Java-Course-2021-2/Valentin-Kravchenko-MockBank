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

    @GetMapping("{id}")
    public ResponseEntity<CreditCard> getById(@PathVariable Integer id) {
        LOGGER.debug("getById(api/card/{})", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        return ResponseEntity.ok(creditCardFromDb);
    }

    @GetMapping("{id}/deposit")
    public ResponseEntity<CreditCardTransactionDto> deposit(@PathVariable Integer id) {
        LOGGER.debug("depositGET(api/card/{}/deposit)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @GetMapping("{id}/transfer")
    public ResponseEntity<CreditCardTransactionDto> transfer(@PathVariable Integer id) {
        LOGGER.debug("transferGET(api/card/{}/transfer)", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(creditCardFromDb.getNumber());
        return ResponseEntity.ok(creditCardTransactionDto);
    }

    @PostMapping
    public ResponseEntity<CreditCard> create(@RequestBody Integer accountId){
        LOGGER.debug("create(api/card, accountId={})", accountId);
        bankAccountService.getById(accountId);
        CreditCard createdCreditCard = creditCardService.create(accountId);
        return ResponseEntity.ok(createdCreditCard);
    }

    @PostMapping("deposit")
    public ResponseEntity<CreditCard> deposit(@Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("depositPOST(api/card/deposit, card={})", creditCardTransactionDto);
        CreditCard targetCreditCard = creditCardService.deposit(creditCardTransactionDto);
        return ResponseEntity.ok(targetCreditCard);
    }

    @PostMapping("transfer")
    public ResponseEntity<CreditCard> transfer(@Valid @RequestBody CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("transferPOST(api/card/transfer, card={})", creditCardTransactionDto);
        CreditCard sourceCreditCard = creditCardService.transfer(creditCardTransactionDto);
        return ResponseEntity.ok(sourceCreditCard);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CreditCard> remove(@PathVariable Integer id){
        LOGGER.debug("remove(api/card/{})", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return ResponseEntity.ok(deletedCreditCard);
    }

}
