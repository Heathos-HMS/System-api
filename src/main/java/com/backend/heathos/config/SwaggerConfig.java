package com.backend.heathos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI hmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Management System API")
                        .description(
                                "Backend API for the HMS Staff Portal. " +
                                        "This system is staff-only — patients do not have accounts. " +
                                        "All endpoints except /api/auth/** require a valid JWT Bearer token. " +
                                        "Roles: ADMIN, RECEPTIONIST, DOCTOR, NURSE, LAB_TECHNICIAN, BILLING_OFFICER, PHARMACIST."
                        )
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("HMS Backend Team")
                                .email("dev@hms.com")
                        )
                )
                // This tells Swagger that the API uses Bearer token authentication
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Paste your JWT token here. Get it from POST /api/auth/login")
                        )
                );
    }
}
