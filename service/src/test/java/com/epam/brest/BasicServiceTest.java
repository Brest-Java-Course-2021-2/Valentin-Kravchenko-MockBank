package com.epam.brest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:service.xml", "classpath*:properties.xml"})
@Transactional
public class BasicServiceTest {
}
