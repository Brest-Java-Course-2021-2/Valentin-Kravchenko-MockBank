package com.epam.brest.service.impl;

import com.epam.brest.dao.api.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankAccountDtoServiceImpl implements BankAccountDtoService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoServiceImpl.class);

    private final BankAccountDtoDao bankAccountDtoDao;

    public BankAccountDtoServiceImpl(BankAccountDtoDao bankAccountDtoDao) {
        this.bankAccountDtoDao = bankAccountDtoDao;
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards() {
        LOGGER.debug("getAllWithTotalCards()");
        return bankAccountDtoDao.getAllWithTotalCards();
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards(BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("getAllWithTotalCards(bankAccountFilterDto={})", bankAccountFilterDto);
        return bankAccountDtoDao.getAllWithTotalCards(bankAccountFilterDto);
    }

}
