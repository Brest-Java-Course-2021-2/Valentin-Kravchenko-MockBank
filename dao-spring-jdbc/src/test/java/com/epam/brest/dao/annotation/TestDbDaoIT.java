package com.epam.brest.dao.annotation;

import com.epam.brest.dao.config.DaoTestConfig;
import com.epam.brest.dao.config.SpringJdbcDaoConfig;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test-db")
@SpringBootTest(classes = {DaoTestConfig.class, SpringJdbcDaoConfig.class},
                properties = {"spring.output.ansi.enabled=always"})
@AutoConfigureDataJdbc
@Transactional
public @interface TestDbDaoIT {
}
