package edu.eci.patricia.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Campus Events Service API")
                        .version("v1.0.0")
                        .description(
                                "This microservice is responsible for managing the full lifecycle of university campus events. " +
                                "It handles event creation, updates, and cancellations by authenticated organizers, " +
                                "as well as student RSVP registration and agenda management. " +
                                "All endpoints are secured via JWT Bearer token authentication."
                        ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtained from the authentication service. " +
                                                "Paste your token in the field below (without the 'Bearer' prefix).")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"));
    }
}
