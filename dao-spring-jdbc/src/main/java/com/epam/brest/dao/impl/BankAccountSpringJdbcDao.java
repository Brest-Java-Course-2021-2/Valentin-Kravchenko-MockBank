package com.epam.brest.dao.impl;

import com.epam.brest.dao.AbstractSpringJdbcDao;
import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.model.entity.BankAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.dao.constant.ColumnName.ID;
import static com.epam.brest.dao.constant.DaoConstant.DELIMITER;

public class BankAccountSpringJdbcDao extends AbstractSpringJdbcDao<BankAccount> implements BankAccountDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<BankAccount> rowMapper;

    @Value("${account.get.all.order}")
    private String getAllSql;

    @Value("${account.get.by.id}")
    private String getByIdSql;

    @Value("${account.get.by.number}")
    private String getByNumberSql;

    @Value("${account.insert}")
    private String insertSql;

    @Value("${account.update}")
    private String updateSql;

    @Value("${account.delete}")
    private String deleteSql;

    @Value("${account.count}")
    private String countSql;

    @Value("${account.count.number}")
    private String countNumberSql;

    @Value("${account.get.cards.number}")
    private String getCardNumbersSql;

    @Value("${account.error.delete}")
    private String deleteErrorMessage;

    public BankAccountSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(BankAccount.class);
    }

    @Override
    public List<BankAccount> getAll() {
        return namedParameterJdbcTemplate.query(getAllSql, rowMapper);
    }

    @Override
    public Optional<BankAccount> getById(Integer id) {
        return getOneById(getByIdSql, id, rowMapper);
    }

    @Override
    public Optional<BankAccount> getByNumber(String number) {
        return getByNumber(getByNumberSql, number, rowMapper);
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        return create(insertSql, bankAccount);
    }

    @Override
    public Integer update(BankAccount bankAccount) {
        return update(updateSql, bankAccount);
    }

    @Override
    public Integer delete(BankAccount bankAccount) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.name(), bankAccount.getId());
        List<String> linkedCards = getLinkedCards(sqlParameterSource);
        if (!linkedCards.isEmpty()) {
            throw new IllegalArgumentException(deleteErrorMessage.formatted(bankAccount.getNumber(),
                                                                            String.join(DELIMITER, linkedCards)));
        }
        return namedParameterJdbcTemplate.update(deleteSql, sqlParameterSource);
    }

    @Override
    public Integer count() {
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }

    @Override
    public boolean isAccountNumberExists(String number) {
        return isNumberExists(countNumberSql, number);
    }

    private List<String> getLinkedCards(SqlParameterSource sqlParameterSource) {
        return namedParameterJdbcTemplate.queryForList(getCardNumbersSql, sqlParameterSource, String.class);
    }

}
