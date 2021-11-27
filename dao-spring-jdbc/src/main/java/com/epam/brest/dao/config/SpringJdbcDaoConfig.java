package com.epam.brest.dao.config;

import config.H2Config;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.brest")
@Import(H2Config.class)
@PropertySource({"classpath:dao.properties"})
public class SpringJdbcDaoConfig {

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
