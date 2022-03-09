package com.epam.brest.restapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                                                                    new ObjectSchema().addProperties("id", new IntegerSchema().example(1))
                                                                                      .addProperties("customer", new StringSchema().example("Ivan Ivanoff"))));
    }

}
