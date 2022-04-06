package com.epam.brest.openapi.impl;

import com.epam.brest.model.*;
import com.epam.brest.openapi.ApiDelegateBasic;
import com.epam.brest.openapi.api.CreditCardApiDelegate;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardDtoService;
import com.epam.brest.service.api.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Locale;

@Service
public class CreditCardApiDelegateImpl extends ApiDelegateBasic implements CreditCardApiDelegate {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardApiDelegateImpl.class);

    private final CreditCardService creditCardService;

    private final CreditCardDtoService creditCardDtoService;

    private final BankAccountService bankAccountService;

    public CreditCardApiDelegateImpl(CreditCardService creditCardServiceImpl,
                                     CreditCardDtoService creditCardDtoServiceImpl,
                                     BankAccountService bankAccountServiceImpl,
                                     Validator validator) {
        super(validator);
        this.creditCardService = creditCardServiceImpl;
        this.creditCardDtoService = creditCardDtoServiceImpl;
        this.bankAccountService = bankAccountServiceImpl;
    }

    @Override
    public ResponseEntity<CreditCard> getCreditCardById(Integer id) {
        LOGGER.debug("getCreditCardById(id={})", id);
        CreditCard creditCardFromDb = creditCardService.getById(id);
        return ResponseEntity.ok(creditCardFromDb);
    }

    @Override
    public ResponseEntity<CreditCard> createCreditCard(Integer bankAccountId) {
        LOGGER.debug("createCreditCard(bankAccountId={})", bankAccountId);
        bankAccountService.getById(bankAccountId);
        CreditCard createdCreditCard = creditCardService.create(bankAccountId);
        return ResponseEntity.ok(createdCreditCard);
    }

    @Override
    public ResponseEntity<CreditCard> deleteCreditCard(Integer id) {
        LOGGER.debug("deleteCreditCard(id={})", id);
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return ResponseEntity.ok(deletedCreditCard);
    }

    @Override
    public ResponseEntity<CreditCard> depositMoney(DepositTransactionDto depositTransactionDto) {
        LOGGER.debug("depositMoney(depositTransactionDto={})", depositTransactionDto);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(depositTransactionDto.getTargetCardNumber());
        creditCardTransactionDto.setValueSumOfMoney(depositTransactionDto.getValueSumOfMoney());
        creditCardTransactionDto.setLocale(new Locale(depositTransactionDto.getLocale()));
        validate(creditCardTransactionDto);
        CreditCard targetCreditCard = creditCardService.deposit(creditCardTransactionDto);
        return ResponseEntity.ok(targetCreditCard);
    }

    @Override
    public ResponseEntity<CreditCard> transferMoney(TransferTransactionDto transferTransactionDto) {
        LOGGER.debug("transferTransactionDto={})", transferTransactionDto);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(transferTransactionDto.getSourceCardNumber());
        creditCardTransactionDto.setTargetCardNumber(transferTransactionDto.getTargetCardNumber());
        creditCardTransactionDto.setValueSumOfMoney(transferTransactionDto.getValueSumOfMoney());
        creditCardTransactionDto.setLocale(new Locale(transferTransactionDto.getLocale()));
        validate(creditCardTransactionDto);
        CreditCard sourceCreditCard = creditCardService.transfer(creditCardTransactionDto);
        return ResponseEntity.ok(sourceCreditCard);
    }

    @Override
    public ResponseEntity<List<CreditCardDto>> getCards() {
        LOGGER.debug("getCards()");
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<List<CreditCardDto>> getFilteredCards(CreditCardFilterDto creditCardFilterDto) {
        LOGGER.debug("getFilteredCards(creditCardDateRangeDto={})", creditCardFilterDto);
        validate(creditCardFilterDto);
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        return ResponseEntity.ok(cards);
    }

}
