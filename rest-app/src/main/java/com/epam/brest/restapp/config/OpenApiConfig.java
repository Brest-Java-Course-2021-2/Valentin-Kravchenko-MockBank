package com.epam.brest.restapp.config;

import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${controller.version}") String version) {

        return new OpenAPI().info(new Info().title("MockBank API")
                                            .description("MockBank is a web-application to work with bank accounts and linked them credit cards")
                                            .version(version)
                                            .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0)")))
                            .externalDocs(new ExternalDocumentation().description("GitHub Repository")
                                                                     .url("https://github.com/Brest-Java-Course-2021-2/Valentin-Kravchenko-MockBank"))
                            .components(new Components().addSchemas("errorMessage",
                                                                    new ObjectSchema().addProperties("message", new StringSchema()))
                                                        .addSchemas("validationErrors",
                                                                    new MapSchema().addProperties("validationErrors", new MapSchema()))
                                                        .addSchemas("createBankAccount",
                                                                    new ObjectSchema().addProperties("customer", new StringSchema().example("Sergey Sergeev")))
                                                        .addSchemas("updateBankAccount",
                                                                    getObjectSchema(Map.of("id", new IntegerSchema().example(1),
                                                                                           "customer", new StringSchema().example("Ivan Ivanoff"))))
                                                        .addSchemas("createCreditCard", new IntegerSchema().example(1))
                                                        .addSchemas("depositMoney",
                                                                    getObjectSchema(Map.of("targetCardNumber",
                                                                                           new StringSchema().example("4000003394112581").description("Number of a target credit card"),
                                                                                           "valueSumOfMoney",
                                                                                           new StringSchema().example("1500,00").description("Value of a sum of money"),
                                                                                           "locale", new StringSchema().example("ru").description("Current locale"))))
                                                        .addSchemas("transferMoney",
                                                                    getResolvedSchema(CreditCardTransactionDto.class,
                                                                                  "locale", new StringSchema().example("ru").description("Current locale")))
                                                        .addSchemas("creditCard",
                                                                    getResolvedSchema(CreditCard.class,
                                                                "balance", new StringSchema().example("1000.00").description("Credit card balance")))
                                                        .addSchemas("creditCardDto",
                                                                    getResolvedSchema(CreditCardDto.class,
                                                                "balance", new StringSchema().example("1000.00").description("Credit card balance"))));
    }

    private Schema getObjectSchema(Map<String, Schema> properties){
        return new ObjectSchema().properties(properties);
    }

    private Schema getResolvedSchema(Class className, String key, Schema propertiesItem){
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                                                       .resolveAsResolvedSchema(new AnnotatedType(className).resolveAsRef(false));
        return resolvedSchema.schema.addProperties(key, propertiesItem);
    }

}
