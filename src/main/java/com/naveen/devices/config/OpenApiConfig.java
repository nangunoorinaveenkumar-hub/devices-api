package com.naveen.devices.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String API_KEY_NAME = "X-API-KEY";

    @Bean
    public OpenAPI devicesApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Devices API")
                        .description("API for managing device resources")
                        .version("1.0.0")
                )
                .addSecurityItem(new SecurityRequirement().addList(API_KEY_NAME))
                .components(new Components()
                        .addSecuritySchemes(API_KEY_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(API_KEY_NAME)
                        )
                );
    }
}