package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CreditCardDtoServiceImpl implements CreditCardDtoService {

    private final CreditCardDtoDao creditCardDtoDao;

    public CreditCardDtoServiceImpl(CreditCardDtoDao creditCardDtoDao) {
        this.creditCardDtoDao = creditCardDtoDao;
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        return creditCardDtoDao.getAllWithAccountNumber();
    }

}
