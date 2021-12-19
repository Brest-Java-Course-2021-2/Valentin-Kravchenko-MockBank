package com.epam.brest.webapp.config;

import com.epam.brest.service.api.BankAccountDtoService;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardDtoService;
import com.epam.brest.service.api.CreditCardService;
import com.epam.brest.service.impl.BankAccountDtoServiceRest;
import com.epam.brest.service.impl.BankAccountServiceRest;
import com.epam.brest.service.impl.CreditCardDtoServiceRest;
import com.epam.brest.service.impl.CreditCardServiceRest;
import com.epam.brest.webapp.formatter.BigDecimalPrinter;
import com.epam.brest.webapp.formatter.ForceNullStringParser;
import com.epam.brest.webapp.formatter.LocalDateFormatter;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@PropertySource({"classpath:controller.properties"})
public class WebAppConfig implements WebMvcConfigurer {

    private final WebClient webClient;

    public WebAppConfig(WebClient webClient) {
        this.webClient = webClient;
    }

    @Bean
    BankAccountService bankAccountServiceRest(@Value("${account.endpoint}") String endpoint) {
        return new BankAccountServiceRest(webClient, endpoint);
    }

    @Bean
    BankAccountDtoService bankAccountDtoServiceRest(@Value("${account.dto.endpoint}") String endpoint) {
        return new BankAccountDtoServiceRest(webClient, endpoint);
    }

    @Bean
    CreditCardService creditCardServiceRest(@Value("${card.endpoint}") String endpoint) {
        return new CreditCardServiceRest(webClient, endpoint);
    }

    @Bean
    CreditCardDtoService creditCardDtoServiceRest(@Value("${card.dto.endpoint}") String endpoint) {
        return new CreditCardDtoServiceRest(webClient, endpoint);
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
         return new LocalDateFormatter();
    }

    @Bean
    public Printer<BigDecimal> bigDecimalPrinter() {
        return new BigDecimalPrinter();
    }

    @Bean
    public Parser<String> forceNullStringParser() {
        return new ForceNullStringParser();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(localDateFormatter());
        registry.addPrinter(bigDecimalPrinter());
        registry.addParser(forceNullStringParser());
    }

    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver springResourceTemplateResolver){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(springResourceTemplateResolver);
        templateEngine.addDialect(new LayoutDialect());
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:templates");
        templateEngine.setTemplateEngineMessageSource(messageSource);
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine springTemplateEngine){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(springTemplateEngine);
        viewResolver.setCharacterEncoding(UTF_8.name());
        return viewResolver;
    }

}
