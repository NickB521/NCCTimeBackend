package com.codedifferently.tsm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("TSM Backend API Documentation")
                        .description("Swagger UI documentation for timesheet management.")
                        .version("1.0.0"))

                .addSecurityItem(new SecurityRequirement().addList("JavaInUseSecurityScheme"))
                .components(
                        new Components()
                                .addSecuritySchemes("JavaInUseSecurityScheme", new SecurityScheme()
                                            .name("JavaInUseSecurityScheme")
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")));

    }

}