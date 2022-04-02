package com.epam.brest.dao.impl;

import com.epam.brest.dao.api.CreditCardDtoDao;
import com.epam.brest.dao.util.DaoUtils;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.CreditCardDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    @Value("${card.dto.get.all.by.date.range}")
    private String getAllSqlByDateRange;

    @Value("#{${card.dto.date.range.where}}")
    private Map<String, String> sqlWhereDateRangeMap;

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        LOGGER.debug("getAllWithAccountNumber()");
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber(CreditCardFilterDto creditCardFilterDto) {
        LOGGER.debug("getAllWithTotalCards(getAllWithAccountNumber={})", creditCardFilterDto);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(creditCardFilterDto);
        LOGGER.info("getAllWithTotalCards(sqlParameterSource={})", sqlParameterSource);
        String dynamicWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, sqlWhereDateRangeMap);
        return namedParameterJdbcTemplate.query(String.format(getAllSqlByDateRange, dynamicWhereSql), sqlParameterSource, rowMapper);
    }

}
