package com.example.thehiveapp.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "The Hive API",
        version = "1.0",
        description = "API documentation for The Hive application"
))
public class SwaggerConfig {

}
