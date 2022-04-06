package com.epam.brest.openapi.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder ->
                builder.postConfigurer(objectMapper ->
                        objectMapper.configOverride(BigDecimal.class)
                                    .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING)));
    }

}
