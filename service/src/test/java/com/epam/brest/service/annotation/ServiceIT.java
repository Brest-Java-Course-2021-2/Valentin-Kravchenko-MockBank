package com.epam.brest.service.annotation;

import com.epam.brest.generator.impl.SimpleBankDataGenerator;
import com.epam.brest.service.config.ServiceTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {ServiceTestConfig.class})
@Import({SimpleBankDataGenerator.class})
@Transactional
public @interface ServiceIT {
}
