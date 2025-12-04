package com.naveen.devices.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void devicesApi_shouldReturnOpenApiObject_withApiKeySecurity() {
        final OpenAPI openAPI = config.devicesApi();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getComponents().getSecuritySchemes())
                .containsKey(OpenApiConfig.API_KEY_NAME);
        assertThat(openAPI.getSecurity()).isNotEmpty();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Devices API");
    }
}
