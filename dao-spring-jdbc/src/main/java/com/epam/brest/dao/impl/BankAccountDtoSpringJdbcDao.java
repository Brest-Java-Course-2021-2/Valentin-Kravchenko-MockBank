package com.epam.brest.dao.impl;

import com.epam.brest.dao.api.BankAccountDtoDao;
import com.epam.brest.dao.util.DaoUtils;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BankAccountDtoSpringJdbcDao implements BankAccountDtoDao {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoSpringJdbcDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<BankAccountDto> rowMapper;

    @Value("${account.dto.get.all}")
    private String getAllSql;

    @Value("${account.dto.get.all.by.filter}")
    private String getAllSqlByFilter;

    @Value("${account.dto.sql.regexp.template}")
    private String sqlRegexpTemplate;

    public BankAccountDtoSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(BankAccountDto.class);
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards() {
        LOGGER.debug("getAllWithTotalCards()");
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards(BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("getAllWithTotalCards(bankAccountFilterDto={})", bankAccountFilterDto);
        SqlParameterSource sqlParameterSource = DaoUtils.getSqlParameterSource(bankAccountFilterDto);
        LOGGER.info("getAllWithTotalCards(sqlParameterSource={})", sqlParameterSource);
        String dynamicWhereSql = DaoUtils.buildDynamicWhereSql(sqlParameterSource, sqlRegexpTemplate);
        return namedParameterJdbcTemplate.query(String.format(getAllSqlByFilter, dynamicWhereSql), sqlParameterSource, rowMapper);
    }

}
