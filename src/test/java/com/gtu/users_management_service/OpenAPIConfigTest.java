package com.gtu.users_management_service;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class OpenAPIConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(OpenAPIConfig.class);

    @Test
    void shouldRegisterOpenAPIBean() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAPI.class);
            OpenAPI openAPI = context.getBean(OpenAPI.class);
            assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearer-jwt");
        });
    }
}
