package com.epam.brest.service.impl;

import com.epam.brest.dao.api.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.api.CreditCardDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreditCardDtoServiceImpl implements CreditCardDtoService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoServiceImpl.class);

    private final CreditCardDtoDao creditCardDtoDao;

    public CreditCardDtoServiceImpl(CreditCardDtoDao creditCardDtoDao) {
        this.creditCardDtoDao = creditCardDtoDao;
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        LOGGER.debug("getAllWithAccountNumber()");
        return creditCardDtoDao.getAllWithAccountNumber();
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber(CreditCardDateRangeDto creditCardDateRangeDto) {
        LOGGER.debug("getAllWithAccountNumber(creditCardDateRangeDto={})", creditCardDateRangeDto);
        return creditCardDtoDao.getAllWithAccountNumber(creditCardDateRangeDto);
    }

}