package com.fiap.cloudcp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("CP2 - DimDim API")
                        .description("API de Clientes e Pedidos (CP2)")
                        .version("v1.0"));
    }
}
