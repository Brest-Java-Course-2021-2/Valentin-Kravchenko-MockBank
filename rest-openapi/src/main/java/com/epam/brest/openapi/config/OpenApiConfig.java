package com.epam.brest.openapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.epam.brest.openapi.constant.OpenApiConstant.*;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenApiCustomiser openApiCustomiser(@Value("${controller.version}") String version) {

        return openApi -> openApi.info(new Info().title(TITLE)
                                                 .description(DESCRIPTION)
                                                 .version(version)
                                                 .license(new License().name(LICENSE)
                                                 .url(LICENSE_URL)))
                                                 .externalDocs(new ExternalDocumentation().description(EXTERNAL_DOCS_DESCRIPTION)
                                                                                          .url(EXTERNAL_DOCS_URL))
                                  .getPaths().forEach((key, value) -> {
                                      if (key.equals("/api/accounts")) {
                                          setNewContent(value, buildNewContentWithArraySchema("BankAccountDto"));
                                      }
                                      if (key.equals("/api/cards")) {
                                          setNewContent(value, buildNewContentWithArraySchema("CreditCardDto"));
                                      }
                                  });
    }

    private void setNewContent(PathItem pathItem, Content content) {
        pathItem.readOperations()
                .stream()
                .flatMap(o -> o.getResponses()
                               .entrySet()
                               .stream()
                               .filter(e -> e.getKey().equals("200")))
                .forEach(e -> e.getValue().setContent(content));
    }

    private Content buildNewContentWithArraySchema(String schema) {
        return new Content().addMediaType(APPLICATION_JSON,
                new MediaType().schema(
                        new ArraySchema().items(new ObjectSchema().$ref(COMPONENTS_SCHEMAS + schema))
                ));
    }

}
