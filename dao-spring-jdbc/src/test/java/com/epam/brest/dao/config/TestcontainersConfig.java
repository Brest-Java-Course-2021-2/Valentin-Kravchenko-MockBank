package com.epam.brest.dao.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;

@TestConfiguration
@Profile("prod-db-test")
public class TestcontainersConfig {

    public static final String POSTGRES_LATEST = "postgres:latest";

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {
        return new PostgreSQLContainer<>(POSTGRES_LATEST).waitingFor(Wait.forListeningPort());
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                                .url(jdbcDatabaseContainer().getJdbcUrl())
                                .driverClassName(jdbcDatabaseContainer().getDriverClassName())
                                .username(jdbcDatabaseContainer().getUsername())
                                .password(jdbcDatabaseContainer().getPassword())
                                .build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

}
