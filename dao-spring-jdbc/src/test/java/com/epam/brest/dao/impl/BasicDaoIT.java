package com.epam.brest.dao.impl;

import com.epam.brest.dao.config.SpringJdbcDaoConfig;
import config.H2Config;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2Config.class, SpringJdbcDaoConfig.class})
@Transactional
public class BasicDaoIT {
}
