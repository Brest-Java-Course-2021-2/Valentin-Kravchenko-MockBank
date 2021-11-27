package com.epam.brest.dao.impl;

import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreditCardDtoSpringJdbcDao implements CreditCardDtoDao {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardDtoSpringJdbcDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<CreditCardDto> rowMapper;

    public CreditCardDtoSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(CreditCardDto.class);
    }

    @Value("${card.dto.get.all}")
    private String getAllSql;

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        LOGGER.debug("getAllWithAccountNumber()");
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

}
