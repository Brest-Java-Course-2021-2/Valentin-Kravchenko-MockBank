package com.epam.brest.model.validator.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@PropertySource({"classpath:regexp.properties"})
public class ValidatorConfig {

    @Bean
    public NumberStyleFormatter numberStyleFormatter() {
        return new NumberStyleFormatter();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:validator");
        messageSource.setDefaultEncoding(UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }

}
