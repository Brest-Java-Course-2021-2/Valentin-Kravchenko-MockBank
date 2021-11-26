package com.epam.brest.controller;

import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitWebConfig(locations = {"classpath*:test-web-app.xml"})
@Transactional
public class BasicControllerTest {
}
