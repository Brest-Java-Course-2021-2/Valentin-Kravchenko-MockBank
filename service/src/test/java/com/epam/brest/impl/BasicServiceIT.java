package com.epam.brest.impl;

import com.epam.brest.config.ServiceConfig;
import com.epam.brest.generator.config.BankDataGeneratorConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BankDataGeneratorConfig.class, ServiceConfig.class})
@Transactional
public class BasicServiceIT {
}
