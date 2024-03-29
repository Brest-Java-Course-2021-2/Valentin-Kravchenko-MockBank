package com.epam.brest.dao.impl;

import com.epam.brest.dao.SpringJdbcDaoBasic;
import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.model.CreditCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.dao.constant.ParamName.ID;

@Repository
public class CreditCardSpringJdbcDao extends SpringJdbcDaoBasic<CreditCard> implements CreditCardDao {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardSpringJdbcDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<CreditCard> rowMapper;

    @Value("${card.get.all.order.by}")
    private String getAllOrderBySql;

    @Value("${card.get.by.id}")
    private String getByIdSql;

    @Value("${card.get.by.number}")
    private String getByNumberSql;

    @Value("${card.insert}")
    private String insertSql;

    @Value("${card.update}")
    private String updateSql;

    @Value("${card.delete}")
    private String deleteSql;

    @Value("${card.count}")
    private String countSql;

    @Value("${card.count.by.number}")
    private String countByNumberSql;

    @Value("${card.get.all.by.account.id}")
    private String getAllByAccountIdSql;

    public CreditCardSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(CreditCard.class);
    }

    @Override
    public List<CreditCard> getAll() {
        LOGGER.debug("getAll()");
        return namedParameterJdbcTemplate.query(getAllOrderBySql, rowMapper);
    }

    @Override
    public Optional<CreditCard> getById(Integer id) {
        LOGGER.debug("getById(id={})", id);
        return getById(getByIdSql, id, rowMapper);
    }

    @Override
    public Optional<CreditCard> getByNumber(String number) {
        LOGGER.debug("getByNumber(number={})", number);
        return getByNumber(getByNumberSql, number, rowMapper);
    }

    @Override
    public CreditCard create(CreditCard creditCard) {
        LOGGER.debug("create(creditCard={})", creditCard);
        return create(insertSql, creditCard);
    }

    @Override
    public Integer update(CreditCard creditCard) {
        LOGGER.debug("update(creditCard={})", creditCard);
        return update(updateSql, creditCard);
    }

    @Override
    public Integer delete(Integer id) {
        LOGGER.debug("delete(id={})", id);
        return delete(deleteSql, id);
    }

    @Override
    public Integer count() {
        LOGGER.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }

    @Override
    public boolean isCardNumberExists(String number) {
        LOGGER.debug("isCardNumberExists(number={})", number);
        return isNumberExists(countByNumberSql, number);
    }

    @Override
    public List<CreditCard> getAllByAccountId(Integer id) {
        LOGGER.debug("getAllByAccountId(id={})", id);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.getName(), id);
        return namedParameterJdbcTemplate.query(getAllByAccountIdSql, sqlParameterSource, rowMapper);
    }

}
