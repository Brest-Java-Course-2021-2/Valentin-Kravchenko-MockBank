package com.epam.brest.service.impl;

import com.epam.brest.dao.api.CreditCardDtoDao;
import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.service.api.CreditCardDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@Transactional(readOnly = true)
public class CreditCardDtoServiceImpl implements CreditCardDtoService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoServiceImpl.class);

    private final CreditCardDtoDao creditCardDtoDao;

    private final DateTimeFormatter dateTimeFormatter;

    public CreditCardDtoServiceImpl(CreditCardDtoDao creditCardDtoDao,
                                    @Value("${card.filter.date.pattern}") String datePattern) {
        this.creditCardDtoDao = creditCardDtoDao;
        dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        LOGGER.debug("getAllWithAccountNumber()");
        return creditCardDtoDao.getAllWithAccountNumber();
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber(CreditCardFilterDto creditCardFilterDto) {
        LOGGER.debug("getAllWithAccountNumber(creditCardDateRangeDto={})", creditCardFilterDto);
        if (nonNull(creditCardFilterDto.getFromDateValue())) {
            creditCardFilterDto.setFromDate(parse(creditCardFilterDto.getFromDateValue()));
        }
        if (nonNull(creditCardFilterDto.getToDateValue())) {
            creditCardFilterDto.setToDate(parse(creditCardFilterDto.getToDateValue()));
        }
        return creditCardDtoDao.getAllWithAccountNumber(creditCardFilterDto);
    }

    private LocalDate parse(String dateValue) {
        LOGGER.trace("parse(dateValue={})", dateValue);
        return YearMonth.parse(dateValue, dateTimeFormatter).atEndOfMonth();
    }

}
