package com.epam.brest.impl;

import com.epam.brest.dao.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.service.BankAccountDtoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class BankAccountDtoServiceImpl implements BankAccountDtoService {

    private final BankAccountDtoDao bankAccountDtoDao;

    public BankAccountDtoServiceImpl(BankAccountDtoDao bankAccountDtoDao) {
        this.bankAccountDtoDao = bankAccountDtoDao;
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards() {
        return bankAccountDtoDao.getAllWithTotalCards();
    }

}
