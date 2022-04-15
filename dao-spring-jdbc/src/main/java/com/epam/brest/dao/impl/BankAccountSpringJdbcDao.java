package com.epam.brest.dao.impl;

import com.epam.brest.dao.SpringJdbcDaoBasic;
import com.epam.brest.dao.annotation.InjectSql;
import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.model.BankAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@InjectSql(prefix = "account")
@Repository
public class BankAccountSpringJdbcDao extends SpringJdbcDaoBasic<BankAccount> implements BankAccountDao {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountSpringJdbcDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<BankAccount> rowMapper;

//    @Value("${account.get.all.order.by}")
    private String getAllOrderBySql;

//    @Value("${account.get.by.id}")
    private String getByIdSql;

//    @Value("${account.get.by.number}")
    private String getByNumberSql;

//    @Value("${account.insert}")
    private String insertSql;

//    @Value("${account.update}")
    private String updateSql;

//    @Value("${account.delete}")
    private String deleteSql;

//    @Value("${account.count}")
    private String countSql;

//    @Value("${account.count.by.number}")
    private String countByNumberSql;

    public BankAccountSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(BankAccount.class);
    }

    @Override
    public List<BankAccount> getAll() {
        LOGGER.debug("getAll()");
        return namedParameterJdbcTemplate.query(getAllOrderBySql, rowMapper);
    }

    @Override
    public Optional<BankAccount> getById(Integer id) {
        LOGGER.debug("getById(id={})", id);
        return getById(getByIdSql, id, rowMapper);
    }

    @Override
    public Optional<BankAccount> getByNumber(String number) {
        LOGGER.debug("getByNumber(number={})", number);
        return getByNumber(getByNumberSql, number, rowMapper);
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        LOGGER.debug("create(bankAccount={})", bankAccount);
        return create(insertSql, bankAccount);
    }

    @Override
    public Integer update(BankAccount bankAccount) {
        LOGGER.debug("update(bankAccount={})", bankAccount);
        return update(updateSql, bankAccount);
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
    public boolean isAccountNumberExists(String number) {
        LOGGER.debug("isAccountNumberExists(number={})", number);
        return isNumberExists(countByNumberSql, number);
    }

}
