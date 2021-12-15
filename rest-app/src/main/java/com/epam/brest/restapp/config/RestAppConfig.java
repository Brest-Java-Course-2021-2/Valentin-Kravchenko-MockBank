package com.epam.brest.restapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource({"classpath:controller.properties"})
public class RestAppConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                         .addModule(new JavaTimeModule())
                         .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                         .build()
                         .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
