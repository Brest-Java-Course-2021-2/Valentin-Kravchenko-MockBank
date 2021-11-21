package com.epam.brest.dao.impl;

import com.epam.brest.dao.AbstractSpringJdbcDao;
import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.model.entity.CreditCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CreditCardSpringJdbcDao extends AbstractSpringJdbcDao<CreditCard> implements CreditCardDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<CreditCard> rowMapper;

    @Value("${card.get.all.order}")
    private String getAllSql;

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

    @Value("${card.count.number}")
    private String countNumberSql;

    public CreditCardSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(CreditCard.class);
    }

    @Override
    public List<CreditCard> getAll() {
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

    @Override
    public Optional<CreditCard> getById(Integer id) {
        return getById(getByIdSql, id, rowMapper);
    }

    @Override
    public Optional<CreditCard> getByNumber(String number) {
        return getByNumber(getByNumberSql, number, rowMapper);
    }

    @Override
    public CreditCard create(CreditCard creditCard) {
        return create(insertSql, creditCard);
    }

    @Override
    public Integer update(CreditCard creditCard) {
        return update(updateSql, creditCard);
    }

    @Override
    public Integer delete(CreditCard creditCard) {
        return delete(deleteSql, creditCard);
    }

    @Override
    public Integer count() {
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }

    @Override
    public boolean isCardNumberExists(String number) {
        return isNumberExists(countNumberSql, number);
    }

}
