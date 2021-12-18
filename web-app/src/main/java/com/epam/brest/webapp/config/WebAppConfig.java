package com.epam.brest.webapp.config;

import com.epam.brest.webapp.formatter.BigDecimalPrinter;
import com.epam.brest.webapp.formatter.LocalDateFormatter;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Printer;
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

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
         return new LocalDateFormatter();
    }

    @Bean
    public Printer<BigDecimal> bigDecimalPrinter() {
        return new BigDecimalPrinter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(localDateFormatter());
        registry.addPrinter(bigDecimalPrinter());
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
