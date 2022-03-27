package com.epam.brest.dao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:dao.properties"})
public class SpringJdbcDaoConfig {
}
