package com.epam.brest.dao;

import com.epam.brest.dao.util.DaoUtils;
import com.epam.brest.model.BasicEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.brest.dao.constant.ColumnName.ID;
import static com.epam.brest.dao.constant.ColumnName.NUMBER;

public abstract class AbstractSpringJdbcDao<T extends BasicEntity> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractSpringJdbcDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AbstractSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<T> getById(String sql, Integer id, RowMapper<T> rowMapper) {
        LOGGER.info("getById(sql={}, id={})", sql, id);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.name(), id);
        return getOne(sql, rowMapper, sqlParameterSource);
    }

    public Optional<T> getByNumber(String sql, String number, RowMapper<T> rowMapper) {
        LOGGER.info("getByNumber(sql={}, number={})", sql, number);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NUMBER.name(), number);
        return getOne(sql, rowMapper, sqlParameterSource);
    }

    public T create(String sql, T entity) {
        LOGGER.info("create(sql={}, entity={})", sql, entity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
        SqlParameterSource sqlParameterSource = DaoUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        entity.setId(id);
        return entity;
    }

    public Integer update(String sql, T entity) {
        LOGGER.info("update(sql={}, entity={})", sql, entity);
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
        SqlParameterSource sqlParameterSource = DaoUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Integer delete(String sql, Integer id) {
        LOGGER.info("delete(sql={}, id={})", sql, id);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.name(), id);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public boolean isNumberExists(String sql, String number) {
        LOGGER.info("isNumberExists(sql={}, number={})", sql, number);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NUMBER.name(), number);
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(total) == 1;
    }

    private Optional<T> getOne(String sql, RowMapper<T> rowMapper, SqlParameterSource sqlParameterSource) {
        LOGGER.info("getOne(sql={}, sqlParameterSource={})", sql, sqlParameterSource);
        List<T> entities = namedParameterJdbcTemplate.query(sql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(entities));
    }

}
