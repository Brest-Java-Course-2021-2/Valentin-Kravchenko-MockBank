package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class SpringJdbcBankAccountDtoDao implements BankAccountDtoDao {

    private static final String SQL_GET_ALL = """
            SELECT BA.ACCOUNT_ID,
                   BA.ACCOUNT_NUMBER,
                   BA.CUSTOMER,
                   BA.REGISTRATION_DATE,
                   COUNT(CC.CARD_ID) AS TOTAL_CARDS
                FROM BANK_ACCOUNT AS BA
                    JOIN CREDIT_CARD AS CC ON BA.ACCOUNT_ID = CC.ACCOUNT_ID
                        GROUP BY BA.ACCOUNT_ID,
                                 BA.ACCOUNT_NUMBER,
                                 BA.CUSTOMER,
                                 BA.REGISTRATION_DATE
                            ORDER BY BA.REGISTRATION_DATE
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<BankAccountDto> rowMapper;

    public SpringJdbcBankAccountDtoDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(BankAccountDto.class);
    }

    @Override
    public List<BankAccountDto> getAllContainingTotalCards() {
        return namedParameterJdbcTemplate.query(SQL_GET_ALL, rowMapper);
    }
    
}
