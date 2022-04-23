package com.epam.brest.excel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:excel.properties"})
public class ExcelConfig {

    @Bean
    @ConfigurationProperties(prefix = "excel.accounts")
    public ExcelSettings accountsExcelSettings(CellSettings headerCellSettings,
                                               CellSettings contentCellSettings) {
        ExcelSettings excelSettings = new ExcelSettings();
        excelSettings.setHeaderSettings(headerCellSettings);
        excelSettings.setContentSettings(contentCellSettings);
        return excelSettings;
    }

    @Bean
    @ConfigurationProperties(prefix = "excel.header")
    public CellSettings headerCellSettings() {
        return new CellSettings();
    }

    @Bean
    @ConfigurationProperties(prefix = "excel.content")
    public CellSettings contentCellSettings() {
        return new CellSettings();
    }

}
