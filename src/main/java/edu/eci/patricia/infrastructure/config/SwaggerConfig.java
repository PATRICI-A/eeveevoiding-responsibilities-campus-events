package edu.eci.patricia.infrastructure.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PATRICI.A — Eventos Universitarios")
                        .description("M09 — API de Eventos Universitarios")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("X-User-Id",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-User-Id"))
                        .addSecuritySchemes("X-User-Role",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-User-Role")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("X-User-Id")
                        .addList("X-User-Role"));
    }

    @Bean
    public OperationCustomizer customHeaders() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("X-User-Id")
                    .description("UUID del usuario autenticado (simulando Gateway)")
                    .required(false)
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()
                            .example("550e8400-e29b-41d4-a716-446655440000")));
            operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("X-User-Role")
                    .description("Rol del usuario (ORGANIZADOR, ESTUDIANTE, ADMINISTRADOR)")
                    .required(false)
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()
                            .example("ORGANIZADOR")));
            return operation;
        };
    }


}