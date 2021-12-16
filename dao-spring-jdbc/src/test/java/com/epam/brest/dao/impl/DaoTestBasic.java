package com.epam.brest.dao.impl;

import com.epam.brest.dao.config.DaoTestConfig;
import com.epam.brest.dao.config.SpringJdbcDaoConfig;
import com.epam.brest.testdb.config.H2Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {DaoTestConfig.class})
@ContextConfiguration(classes = {H2Config.class, SpringJdbcDaoConfig.class})
@Transactional
public class DaoTestBasic {
}
