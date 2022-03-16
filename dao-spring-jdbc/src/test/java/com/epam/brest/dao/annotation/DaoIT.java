package com.epam.brest.dao.annotation;

import com.epam.brest.dao.config.DaoTestConfig;
import com.epam.brest.dao.config.SpringJdbcDaoConfig;
import com.epam.brest.testdb.config.H2Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {DaoTestConfig.class, H2Config.class, SpringJdbcDaoConfig.class})
@Transactional
public @interface DaoIT {
}
