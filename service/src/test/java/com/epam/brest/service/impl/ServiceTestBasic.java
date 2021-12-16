package com.epam.brest.service.impl;

import com.epam.brest.generator.impl.SimpleBankDataGenerator;
import com.epam.brest.service.config.ServiceTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {ServiceTestConfig.class})
@ContextConfiguration(classes = {ServiceTestConfig.class})
@Import({SimpleBankDataGenerator.class})
@Transactional
public class ServiceTestBasic {
}

