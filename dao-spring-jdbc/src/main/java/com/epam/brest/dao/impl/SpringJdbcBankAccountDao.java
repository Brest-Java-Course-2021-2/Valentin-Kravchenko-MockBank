package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.dao.util.SqlUtils;
import com.epam.brest.model.entity.BankAccount;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.brest.dao.constant.ColumnName.ACCOUNT_ID;
import static com.epam.brest.dao.constant.ColumnName.ACCOUNT_NUMBER;

public class SpringJdbcBankAccountDao implements BankAccountDao {

    private static final String SQL_GET_ALL = """
            SELECT BA.ACCOUNT_ID,
                   BA.ACCOUNT_NUMBER,
                   BA.CUSTOMER,
                   BA.REGISTRATION_DATE
                FROM BANK_ACCOUNT AS BA
                    ORDER BY BA.REGISTRATION_DATE
            """;

    private static final String SQL_GET_ONE = """
            SELECT BA.ACCOUNT_ID,
                   BA.ACCOUNT_NUMBER,
                   BA.CUSTOMER,
                   BA.REGISTRATION_DATE
                FROM BANK_ACCOUNT AS BA
                    WHERE BA.ACCOUNT_ID = :ACCOUNT_ID
            """;

    private static final String SQL_INSERT = """
            INSERT INTO BANK_ACCOUNT (ACCOUNT_NUMBER, CUSTOMER, REGISTRATION_DATE)
                VALUES (:ACCOUNT_NUMBER, :CUSTOMER, :REGISTRATION_DATE)
            """;

    private static final String SQL_UPDATE = """
            UPDATE BANK_ACCOUNT
                SET CUSTOMER = :CUSTOMER
                    WHERE ACCOUNT_ID = :ACCOUNT_ID
            """;

    private static final String SQL_DELETE = "DELETE FROM BANK_ACCOUNT WHERE ACCOUNT_ID = :ACCOUNT_ID";

    private static final String SQL_GET_CARDS = """
            SELECT CC.CARD_NUMBER
                FROM BANK_ACCOUNT AS BA
                    JOIN CREDIT_CARD AS CC ON BA.ACCOUNT_ID = CC.ACCOUNT_ID         
                        WHERE BA.ACCOUNT_ID = :ACCOUNT_ID
            """;

    private static final String SQL_COUNT = "SELECT COUNT(*) from BANK_ACCOUNT";

    private static final String SQL_COUNT_ACCOUNT_NUMBER = """
             SELECT COUNT(*)
                FROM BANK_ACCOUNT AS BA
                    WHERE BA.ACCOUNT_NUMBER = :ACCOUNT_NUMBER
            """;
    public static final String ACCOUNT_CANNOT_BE_DELETED = "Account cannot be deleted because the following credit cards [%s] are linked with it!";
    public static final String DELIMITER = ", ";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<BankAccount> rowMapper;

    public SpringJdbcBankAccountDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(BankAccount.class);
    }

    @Override
    public List<BankAccount> getAll() {
        return namedParameterJdbcTemplate.query(SQL_GET_ALL, rowMapper);
    }

    @Override
    public Optional<BankAccount> getOneById(Integer accountId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ACCOUNT_ID.name(), accountId);
        List<BankAccount> bankAccounts = namedParameterJdbcTemplate.query(SQL_GET_ONE, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(bankAccounts));
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(bankAccount);
        SqlParameterSource sqlParameterSource = SqlUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder);
        Integer accountId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        bankAccount.setAccountId(accountId);
        return bankAccount;
    }

    @Override
    public Integer update(BankAccount bankAccount) {
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(bankAccount);
        SqlParameterSource sqlParameterSource = SqlUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        return namedParameterJdbcTemplate.update(SQL_UPDATE, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer accountId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ACCOUNT_ID.name(), accountId);
        List<String> linkedCards = getLinkedCards(sqlParameterSource);
        if (!linkedCards.isEmpty()) {
            throw new IllegalArgumentException(ACCOUNT_CANNOT_BE_DELETED.formatted(String.join(DELIMITER, linkedCards)));
        }
        return namedParameterJdbcTemplate.update(SQL_DELETE, sqlParameterSource);
    }

    @Override
    public Integer count() {
        return namedParameterJdbcTemplate.queryForObject(SQL_COUNT, new HashMap<>(), Integer.class);
    }

    @Override
    public boolean isAccountNumberExists(String accountNumber) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ACCOUNT_NUMBER.name(), accountNumber);
        Integer total = namedParameterJdbcTemplate.queryForObject(SQL_COUNT_ACCOUNT_NUMBER, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(total) == 1;
    }

    private List<String> getLinkedCards(SqlParameterSource sqlParameterSource) {
        return namedParameterJdbcTemplate.queryForList(SQL_GET_CARDS, sqlParameterSource, String.class);
    }

}
