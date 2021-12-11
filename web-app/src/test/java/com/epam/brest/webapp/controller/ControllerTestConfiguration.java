package com.epam.brest.webapp.controller;

import com.epam.brest.webapp.WebApp;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {WebApp.class})
@AutoConfigureMockMvc
@Transactional
public class ControllerTestConfiguration {
}
