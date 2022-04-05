package com.epam.brest.restapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Controller Version", description = "The Controller Version API")
@RestController
@RequestMapping("api/version")
public class VersionRestController {

    @Operation(summary = "Get a controller version",
               operationId = "getVersion",
               responses = {@ApiResponse(content = @Content(schema = @Schema(type = "string", description = "version", example = "1.0")),
                                         responseCode = "200")})
    @GetMapping
    public String getVersion(
            @Parameter(hidden = true)
            @Value("${controller.version}") String version
    ) {
        return version;
    }

}
