package com.epam.brest.dao;

import com.epam.brest.dao.util.DaoUtils;
import com.epam.brest.model.BaseEntity;
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

public abstract class AbstractSpringJdbcDao<T extends BaseEntity> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AbstractSpringJdbcDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<T> getById(String sql, Integer id, RowMapper<T> rowMapper) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.name(), id);
        return getOne(sql, rowMapper, sqlParameterSource);
    }

    public Optional<T> getByNumber(String sql, String number, RowMapper<T> rowMapper) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NUMBER.name(), number);
        return getOne(sql, rowMapper, sqlParameterSource);
    }

    public T create(String sql, T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
        SqlParameterSource sqlParameterSource = DaoUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        entity.setId(id);
        return entity;
    }

    public Integer update(String sql, T entity) {
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
        SqlParameterSource sqlParameterSource = DaoUtils.extractSqlParameterSource(beanPropertySqlParameterSource);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Integer delete(String sql, T entity) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID.name(), entity.getId());
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public boolean isNumberExists(String sql, String number) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NUMBER.name(), number);
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(total) == 1;
    }

    private Optional<T> getOne(String sql, RowMapper<T> rowMapper, SqlParameterSource sqlParameterSource) {
        List<T> entities = namedParameterJdbcTemplate.query(sql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(entities));
    }

}
