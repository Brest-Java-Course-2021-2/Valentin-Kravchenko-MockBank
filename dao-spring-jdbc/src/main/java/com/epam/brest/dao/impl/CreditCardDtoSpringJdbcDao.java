package com.epam.brest.dao.impl;

import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class CreditCardDtoSpringJdbcDao implements CreditCardDtoDao {

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
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

}
