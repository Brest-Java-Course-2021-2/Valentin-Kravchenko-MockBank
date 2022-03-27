package com.epam.brest.dao.annotation;

import com.epam.brest.dao.config.DaoTestConfig;
import com.epam.brest.dao.config.SpringJdbcDaoConfig;
import com.epam.brest.dao.config.TestcontainersConfig;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("prod-db-test")
@SpringBootTest(classes = {DaoTestConfig.class, SpringJdbcDaoConfig.class, TestcontainersConfig.class},
                properties = {"spring.output.ansi.enabled=always"})
@Sql({"classpath:db/changelog/prod-db-schema.sql", "classpath:sql/test-db-data.sql"})
@Transactional
public @interface DaoITWithProdDb {
}