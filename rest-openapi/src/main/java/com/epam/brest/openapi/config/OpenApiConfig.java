package com.epam.brest.openapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.epam.brest.openapi.constant.OpenApiConstant.*;

@Configuration
public class OpenApiConfig {
    @Bean
    public static OpenApiCustomiser openApiCustomiser(@Value("${controller.version}") String version) {

        PathItem pathItem = new PathItem().get(
                new Operation()
                        .operationId("getAccounts")
                        .tags(List.of("Bank Account"))
                        .summary("List of all bank accounts")
                        .responses(
                                new ApiResponses().addApiResponse("default",
                                        new ApiResponse().content(
                                                new Content().addMediaType("application/json",
                                                        new MediaType().schema(
                                                                new ArraySchema().items(new ObjectSchema().$ref("#/components/schemas/BankAccountDto"))
                                                        ))))));


        return openApi -> openApi.info(new Info().title(TITLE)
                                                 .description(DESCRIPTION)
                                                 .version(version)
                                                 .license(new License().name(LICENSE).url(LICENSE_URL)))
                                 .externalDocs(new ExternalDocumentation().description(EXTERNAL_DOCS_DESCRIPTION)
                                                                          .url(EXTERNAL_DOCS_URL))/*
                                 .tags(List.of(new Tag().name("Bank Account").description("The Bank Account API"),
                                               new Tag().name("Credit Card").description("The Credit Card API"),
                                               new Tag().name("Controller Version").description("The Controller Version API")))*/
                                 .path("/api/accounts", pathItem);
    }

}
