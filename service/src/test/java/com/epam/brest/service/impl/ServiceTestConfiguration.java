package com.epam.brest.service.impl;

import com.epam.brest.generator.impl.SimpleBankDataGenerator;
import com.epam.brest.service.config.ServiceConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {ServiceConfig.class})
@ContextConfiguration(classes = {ServiceConfig.class})
@Import({SimpleBankDataGenerator.class})
@Transactional
public class ServiceTestConfiguration {
}

