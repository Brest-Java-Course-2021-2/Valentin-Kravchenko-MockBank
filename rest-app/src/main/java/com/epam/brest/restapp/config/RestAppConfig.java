package com.epam.brest.restapp.config;

import com.epam.brest.restapp.serializer.BidDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;

@Configuration
@PropertySource({"classpath:controller.properties"})
public class RestAppConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule bigDecimalModule = new SimpleModule();
        bigDecimalModule.addSerializer(BigDecimal.class, new BidDecimalSerializer());
        return JsonMapper.builder()
                         .addModule(new JavaTimeModule())
                         .addModule(bigDecimalModule)
                         .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                         .build()
                         .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
