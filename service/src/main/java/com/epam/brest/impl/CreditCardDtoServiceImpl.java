package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
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

}
