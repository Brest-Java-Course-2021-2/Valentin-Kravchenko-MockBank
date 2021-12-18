package com.epam.brest.webapp.controller;

import com.epam.brest.webapp.WebApp;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {WebApp.class})
@AutoConfigureMockMvc
@Transactional
public class ControllerTestBasic {

    public static final String ACCOUNTS_ENDPOINT = "/accounts";
    public static final String CARDS_ENDPOINT = "/cards";

    private final MockMvc mockMvc;

    public ControllerTestBasic(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    ResultActions performGetAndExpectStatusOk(String endpoint, String viewName) throws Exception {
        return mockMvc.perform(get(endpoint))
                      .andExpect(status().isOk())
                      .andExpect(content().contentType("text/html;charset=UTF-8"))
                      .andExpect(view().name(viewName));

    }

    ResultActions performPostAndExpectStatusOk(String endpoint,
                                               MultiValueMap<String, String> params,
                                               String viewName) throws Exception {
        return mockMvc.perform(post(endpoint).params(params))
                      .andExpect(status().isOk())
                      .andExpect(content().contentType("text/html;charset=UTF-8"))
                      .andExpect(view().name(viewName));
    }

    ResultActions performPostAndExpectStatus3xxRedirection(String endpoint,
                                                           MultiValueMap<String, String> params,
                                                           String viewName,
                                                           String redirectUrl) throws Exception {
        return mockMvc.perform(post(endpoint).params(params))
                      .andExpect(status().is3xxRedirection())
                      .andExpect(view().name(viewName))
                      .andExpect(redirectedUrl(redirectUrl));
    }

}
