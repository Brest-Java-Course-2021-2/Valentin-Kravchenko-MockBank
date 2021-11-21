package com.epam.brest.dao.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:dao-spring-jdbc.xml", "classpath*:test-properties.xml"})
@Transactional
public class BasicDaoIT {
}
