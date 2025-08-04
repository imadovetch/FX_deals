package org.bloomberg.fx_deals.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI fxDealsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("FX Deals API")
                        .description("API for FX Deals management")
                        .version("v1.0"));
    }
}